(ns pissaz.routes
  (:require
    [compojure.core :refer [routes GET POST context]]
    [compojure.route :refer [not-found resources]]
    [selmer.parser :refer [render-file]]
    [pissaz.pages :as page]
    [pissaz.quiz :refer [question1]]))

(selmer.parser/cache-off!)



(def all-routes
  (routes
    (GET "/" req (page/homepage))
    (GET "/contact" req (page/contact))
    (GET "/quizzes" req (page/quizzes))
    (GET "/quiz/:id" req (page/quiz (get-in req [:params :id])))
    (GET "/question" req
      (page/question question1))
    (POST "/answer-check" req (str (req :params)))
    (resources "public/")
    (not-found "not found")))
