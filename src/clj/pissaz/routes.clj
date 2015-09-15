(ns pissaz.routes
  (:require
    [compojure.core :refer [defroutes routes GET POST]]
    [compojure.route :as route]))

(def all-routes
  (routes
    (GET "/" [] "Hello World")
    (route/not-found "Not Found and dumbfounded")))
