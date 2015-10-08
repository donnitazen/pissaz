(ns pissaz.routes
  (:require
    [compojure.core :refer [routes GET POST context]]
    [compojure.route :refer [not-found resources]]
    [selmer.parser :refer [render-file]]
    [pissaz.pages :as page]
    [pissaz.quiz :as quiz]
    [pissaz.users :as users]
    [noir.response :as resp]
    [noir.session :as session]))

(selmer.parser/cache-off!)

(defn signed-in?
  []
  (session/get :username))

(def all-routes
  (routes
   (GET "/" req
     (page/homepage))
   (GET "/contact" req
        (page/contact))
   (GET "/sign-in" req
      (page/sign-in))
   (GET "/sign-up" req
     (page/sign-up))
   (POST "/add-user" req
     (do (session/put! :username (users/add-user (req :params)))
         (resp/redirect "/questions")))
   (GET "/sign-out" req
     (do (session/clear!)
         (page/homepage)))
   (GET "/quizzes" req
        (page/quizzes))
   (GET "/quiz/:id" req
        (page/quiz (get-in req [:params :id])))
   (GET "/questions" req
        (page/questions))
   (GET "/questions/last" req
        (page/questions "Good job on the last question!"))
   (GET "/question/:id" req
        (page/question (get-in req [:params :id])))
   (POST "/answer-check2" req
         (str (req :params)))
   (POST "/answer-check" req
         (let [q-id (get-in req [:params :q-id])
               check (quiz/answer-check (get-in req [:params :type])
                                        (get-in req [:params "1"])
                                        (get-in req [:params :intel]))]
           (if check
             (if (quiz/last-question? q-id)
               (resp/redirect "/questions/last")
               (resp/redirect (str "/question/" (str (inc (read-string q-id))))))
             (resp/redirect (str "/question/" q-id)))))
   (POST "/add-question" req
         (let [new-id (quiz/add-question (req :params))]
           (resp/redirect (str "/question/" new-id))))
   (GET "/numpang/:something" req
        (str (map #(str "</hr>" %) req)))
   (resources "public/")
   (not-found "not found")))


