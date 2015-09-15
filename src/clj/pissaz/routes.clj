(ns pissaz.routes
  (:require
    [compojure.core :refer [routes GET POST context]]
    [compojure.route :refer [not-found resources]]
    [selmer.parser :refer [render-file]]
    [pissaz.quiz :refer [show-question answer-check question1]]))

(selmer.parser/cache-off!)

(defn home-page
  []
  (render-file "public/index.html" {}))

(defn soal-page
  [question]
  (render-file "public/question.html" (show-question question)))

(def all-routes
  (routes
    (context "/" req
             (GET "/" req (home-page)))
    (GET "/question" req (soal-page question1))
    (POST "/answer-check" req (str (req :params)))
    (resources "public/")
    (not-found "not found")))
