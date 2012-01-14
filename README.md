# piccup

Hiccup for clojurescript.

Extracted from https://github.com/ibdknox/pinot

Motivation: 

pinot has a dependency on goog.dom.query which is not part of the std 
clojurescript closure library.

This lib is only for hiccup style rendering. No other dom utils.

## Usage
```
(ns your.ns
  (:require [piccup.html :as ph]))

(def my-dom-element     (first (ph/html [:div "hi"])))
(def my-dom-element-seq (ph/html [:div "hi"] [:div "there"]))
```

## JAR

Clojars: [piccup "1.0.0"] - old property access syntax
Clojars: [piccup "1.1.0"] - new property access syntax


## Compilation

to include this lib in your cljs builds,

* git clone to some\_dir

* in you project, make a dir cljs\_checkouts

* link some\_dir into cljs\_checkouts

Use this version of cljs-watch to build your project: 

* https://github.com/davesann/cljs-watch 


## License

Copyright (C) 2011 Chris Granger

Copyright (C) 2011 Dave Sann (just a small bit)

Distributed under the Eclipse Public License, the same as Clojure.

