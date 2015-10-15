(ns pissaz.routes
  (:require
    [compojure.core :refer [routes GET POST context]]
    [compojure.route :refer [not-found resources]]
    [selmer.parser :refer [render-file]]
    [pissaz.pages :as page]
    [pissaz.quiz :as quiz]
    [pissaz.users :as users]
    [noir.response :as resp]
    [noir.session :as session]
    [pissaz.users :as user]))

(selmer.parser/cache-off!)


(def all-routes-x
  (routes
    (GET "/" req
      (let [user (session/get :username)]
        (if user
          (page/homepage user)
          (page/homepage))))
    (GET "/sign-out" req
      (do (session/clear!)
          (page/homepage)))
    (GET "/sign-in" req
      (page/sign-in))
    (POST "/add-session" req
      (let [username (get-in req [:params :username])
            pass (get-in req [:params :password])]
        (if (users/registered? username pass)
          (do (session/put! :username username)
              (resp/redirect "/"))
          (resp/redirect "/sign-in"))))
    (GET "/profile" req
      (let [user (session/get :username)]
        (if user
          (page/profile user)
          (resp/redirect "/sign-in"))))
    (GET "/sign-up" req
      (page/sign-up))
    (POST "/add-user" req
      (let [user-data (req :params)]
        (if (users/sign-up-validated? user-data)
          (do (session/put! :username (user/add-user user-data))
              (resp/redirect "/"))
          (resp/redirect "/sign-up"))))
    (GET "/quizzes" req
      (let [user (session/get :username)]
        (if user
          (page/quizzes user)
          (page/sign-in))))
    (GET "/quiz/:id" req
      (let [user (session/get :username)]
        (if user
          (page/quiz user (get-in req [:params :id]))
          (page/sign-in))))
    (GET "/questions" req
      (let [user (session/get :username)]
        (if user
          (page/questions user)
          (page/sign-in))))
    (GET "/questions/last" req
      (let [user (session/get :username)]
        (if user
          (page/questions user "Good job on the last question!"))))
    (GET "/question/:id" req
      (let [user (session/get :username)]
        (if user
          (page/question user (get-in req [:params :id]))
          (page/sign-in))))
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
      (let [user? (session/get :username)
            admin? (user/admin? user?)]
        (if admin?
          (resp/redirect (str "/question/" (quiz/add-question (req :params))))
          (resp/redirect "/quizzes"))))
    (resources "public/")
    (not-found "not-found")))
