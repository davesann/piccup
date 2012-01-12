(ns piccup.macros.macros)

(defmacro defelem
  "Defines a function that will return a tag vector. If the first argument
  passed to the resulting function is a map, it merges it with the attribute
  map of the returned tag value."
  [name & fdecl]
  `(let [func# (fn ~@fdecl)]
    (def ~name (phiccup.tags/add-optional-attrs func#))))
