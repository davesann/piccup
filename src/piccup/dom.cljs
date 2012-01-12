(ns piccup.dom)

(defn ensure-coll [c]
  (if (coll? c) c [c]))

(defn attr 
  ([elem attrs]
   (if-not (map? attrs)
     (. elem (getAttribute (name attrs)))
     (do
       (doseq [[k v] attrs]
         (attr elem k v))
       elem)))
  ([elem k v]
   (doseq [el (ensure-coll elem)]
     (. el (setAttribute (name k) v)))
   elem))

