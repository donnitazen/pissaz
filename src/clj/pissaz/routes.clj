(ns pissaz.routes
  (:require
    [compojure.core :refer [routes GET POST context]]
    [compojure.route :refer [not-found resources]]
    [selmer.parser :refer [render-file]]))

(selmer.parser/cache-off!)

(defn home-page
  []
  (render-file "public/index.html" {}))

(def all-routes
  (routes
    (context "/" req
             (GET "/" req (home-page)))
    (resources "public/")
    (not-found "not found")))
