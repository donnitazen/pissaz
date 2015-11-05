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
    [pissaz.users :as user]
    [com.stuartsierra.component :as component]))

(selmer.parser/cache-off!)
(declare all-routes)

(defrecord Routes [database public-directory]
  component/Lifecycle
  (start [this]
    (assoc this
      :tabel (all-routes database public-directory)))
  (stop [this]
    this))

(defn create
  [public-directory]
  (component/using
    (map->Routes {:public-directory public-directory})
    [:database]))

;;;
(defn- misc-routes
  [public-directory]
  (routes
    (resources public-directory)
    (not-found "Fucks given none")))

(defn- user-routes
  [database]
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
        (if (user/registered? username pass (:user-db database))
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
        (if (users/sign-up-validated? user-data (:user-db database))
          (do (session/put! :username (user/add-user user-data))
              (resp/redirect "/"))
          (resp/redirect "/sign-up"))))))

(defn- quiz-routes
  [database]
  (routes
    (GET "/quizzes" req
      (let [user (session/get :username)]
        (if user
          (page/quizzes user (:quiz-db database))
          (page/sign-in))))
    (GET "/quiz/:id" req
      (let [user (session/get :username)]
        (if user
          (page/quiz user (get-in req [:params :id]) (:quiz-db database))
          (page/sign-in))))
    (GET "/questions" req
      (let [user (session/get :username)]
        (if user
          (page/questions user (:question-db database))
          (page/sign-in))))
    (GET "/questions/last" req
      (let [user (session/get :username)]
        (if user
          (page/questions user "Good job on the last question!" (:question-db database)))))
    (GET "/quiz/:quiz_id/question/:id" req
      (let [user (session/get :username)]
        (if user
          (page/homepage user)
          ; (page/quiz-question user (get-in req [:params :quiz_id]) (get-in req [:params :id]))
          (page/sign-in))))
    (POST "/answer-check" req
      (let [q-id (get-in req [:params :q-id])
            check (quiz/answer-check (get-in req [:params :type])
                                     (get-in req [:params "1"])
                                     (get-in req [:params :intel]))]
        (if check
          (if (quiz/last-question? q-id (:question-db database))
            (resp/redirect "/questions/last")
            (resp/redirect (str "/question/" (str (inc (read-string q-id))))))
          (resp/redirect (str "/question/" q-id)))))
    (POST "/add-question" req
      (let [user? (session/get :username)
            admin? (user/admin? user? (:user-db database))]
        (if admin?
          (resp/redirect (str "/question/" (quiz/add-question (req :params) (:question-db database))))
          (resp/redirect "/quizzes"))))))

(defn- all-routes
  [database public-directory]
  (routes (user-routes database)
          (quiz-routes database)
          (misc-routes public-directory)))
