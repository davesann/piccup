(ns piccup.html
  (:require 
    [goog.dom :as gdom]
    [piccup.dom :as pdom]
    [clojure.string :as string]
  ))

(def xmlns {:xhtml "http://www.w3.org/1999/xhtml"
            :svg "http://www.w3.org/2000/svg"})

;; ********************************************
;; Element creation via Hiccup-like vectors 
;; ********************************************

(declare elem-factory)
(def elem-id (atom 0))
(def group-id (atom 0))

(defn as-content [parent content]
  (doseq[c content]
    (let [child (cond
                  (nil? c) nil
                  (map? c) (throw "Maps cannot be used as content")
                  (string? c) (gdom/createTextNode c)
                  (vector? c) (elem-factory c)
                  ;;TODO: there's a bug in clojurescript that prevents seqs from
                  ;; being considered collections
                  (seq? c) (as-content parent c)
                  (.nodeName c) c)]
      (when child
        (gdom/appendChild parent child)))))

;; From Weavejester's Hiccup: https://github.com/weavejester/hiccup/blob/master/src/hiccup/core.clj#L57
(def ^{:doc "Regular expression that parses a CSS-style id and class from a tag name." :private true}
  re-tag #"([^\s\.#]+)(?:#([^\s\.#]+))?(?:\.([^\s#]+))?")

(defn- normalize-element
  "Ensure a tag vector is of the form [tag-name attrs content]."
  [[tag & content]]
  (when (not (or (keyword? tag) (symbol? tag) (string? tag)))
    (throw (str tag " is not a valid tag name.")))
  (let [[_ tag id class] (re-matches re-tag (name tag))
        [nsp tag]     (let [[nsp t] (string/split tag #":")
                               ns-xmlns (xmlns (keyword nsp))]
                           (if t
                             [(or ns-xmlns nsp) t]
                             [(:xhtml xmlns) nsp]))
        tag-attrs        (into {} 
                               (filter #(not (nil? (second %)))
                                       {:id (or id nil)
                                        :class (if class (string/replace class #"\." " "))}))
        map-attrs        (first content)]
    (if (map? map-attrs)
      [nsp tag (merge tag-attrs map-attrs) (next content)]
      [nsp tag tag-attrs content])))


(defn parse-content [elem content]
  (let [attrs (first content)]
  (if (map? attrs)
    (do
      (pdom/attr elem attrs)
      (rest content))
    content)))

(defn create-elem [nsp tag]
  (. js/document (createElementNS nsp tag)))

(defn elem-factory [tag-def]
  (let [[nsp tag attrs content] (normalize-element tag-def)
        elem (create-elem nsp tag)]
    (pdom/attr elem (merge attrs {:puid (swap! elem-id inc)}))
    (as-content elem content)
    elem))

(defn html [& tags]
  (map elem-factory tags))

