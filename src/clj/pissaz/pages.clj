(ns pissaz.pages
  (:require
    [hiccup.core :as hc]
    [hiccup.page :as hp]
    [pissaz.quiz :as quiz]
    [pissaz.users :as user]))


(defn- head
  [title]
  [:head
   [:title title]
   [:meta {:charset "utf-8"}]
   [:link {:rel "stylesheet" :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"}]
   [:link {:rel "stylesheet" :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"}]
   [:link {:rel "stylesheet" :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"}]])



(defn- header
  ([user]
   [:header
    [:ul {:class "nav nav-pills"}
     [:li {:role "presentation"}
      [:a {:href "/"} "Pissaz"]]
     [:li {:role "presentation"}
      [:a {:href "/quizzes"} "Quizzes"]]
     [:li {:role "presentation"}
      [:a {:href "/questions"} "Questions"]]]
    [:ul {:class "nav nav-pills navbar-right"}
     [:li {:role "Hello User"}
      [:a {:href "/profile"} (str "Hello, " user)]]
     [:li {:role "presentation"}
      [:a {:href "/sign-out"} "Sign Out"]]]])
  ([]
   [:header
    [:ul {:class "nav nav-pills"}
     [:li {:role "presentation"}
      [:a {:href "/"} "Pissaz"]]
     [:li {:role "presentation"}
      [:a {:href "/quizzes"} "Quizzes"]]
     [:li {:role "presentation"}
      [:a {:href "/questions"} "Questions"]]]
    [:ul {:class "nav nav-pills navbar-right"}
     [:li {:role "Sign In"}
      [:a {:href "/sign-in"} "Sign In"]]
     [:li {:role "presentation"}
      [:a {:href "/sign-up"} "Sign Up"]]]]))


(defn- footer
  []
  [:div {:class "panel-footer"}
   [:div {:class "col-md-3"}
    [:a {:href "/contact"} "Contact Me"]]])

(defn- body
  ([user anything]
   [:body {:class "container"}
    [:div {:class "row"}
     (header user)]
    anything
    [:div {:class "row"}
     (footer)]])
  ([anything]
   [:body {:class "container"}
    [:div {:class "row"}
     (header)]
    anything
    [:div {:class "row"}
     (footer)]]))

(defn homepage
  ([user]
   (hp/html5 (head "Pissaz - Home")
             (body user [:div {:class "row"}
                          [:div {:class "jumbotron"}
                           [:h1 "Jumbo Pic"]]
                           [:p "bla bla bla about Pissaz"]])))
  ([]
   (hp/html5 (head "Pissaz - Home")
             (body [:div {:class "row"}
                         [:div {:class "jumbotron"}
                          [:h1 "Jumbo Pic"]]
                         [:p "bla bla bla about Pissaz"]]))))

(defn profile
  [user]
  (hp/html5 (head (str "Profile" user))
            (body user [:p "My profile"])))

(defn contact
  []
  (hp/html5 (head "Pissaz - Contact")
            (body [:div {:class "row"}
                   [:p "Call me"]])))

(defn quizzes
  [user]
  (hp/html5 (head "Pissaz All Quizzes")
            (body user
                  [:div {:class "row"}
                   [:div {:class "col-md-2"}
                    [:ul {:class "nav nav-pills nav-stacked"}
                     [:li {:role "presentation"}
                      [:a {:href "/quiz/1"} "Quiz 1"]]]]
                   [:div {:class "col-md-10"}
                    [:h3 "All Quizzes Broh"]
                    [:button {:type "submit" :class "btn btn-success"}
                     "Add a quiz"]
                    [:br]]])))

(defn quiz
  [user id]
  "Sementara quiz 1 dulu lah yah"
  (let [the-quiz id]
    (hp/html5 (head (str "Quiz #" id))
              (body user
                    [:div {:class "row"}
                     [:div {:class "col-md-2"}
                      [:ul {:class "nav nav-pills nav-stacked"}
                       [:li {:role "presentation"}
                        [:a {:href "quiz/1"}  "Quiz 1"]]]]
                     [:div {:class "col-md-10"}
                      [:h2 "Belom ada"]]]))))


(defn sa-or-ma-options
  [a-choice type]
  (cond
    (= type "sa") (hc/html [:input {:type "radio" :name "1" :value (a-choice :idx)} (a-choice :string)]
                           [:br])
    (= type "ma") (hc/html [:input {:type "checkbox" :name "1" :value (a-choice :idx)} (a-choice :string)]
                           [:br])))

(declare quiz-form)

(defn questions
  "to show all questions (samples)"
  ([user pesan]
   (let [all-questions (quiz/read-question-file "question.edn")
         question-list (fn [que] (hc/html [:li {:role "presentation"}
                                           [:a {:href (str "/question/" (que :question-id))} (str "Question nomor " (que :question-id))]]))]
     (hp/html5 (head "Pissaz All Questions")
               (body user [:div {:class "row"}
                      [:div {:class "col-md-2"}
                       [:ul {:class "nav nav-pills nav-stacked"}
                        (map question-list all-questions)]]
                      [:div {:class "col-md-10"}
                       [:h1 pesan]
                       [:h3 "All Quizzes Broh"]
                       (quiz-form)
                       [:br]]]))))
  ([user ] (questions user [""])))

(defn question
  "to show question (sample)"
  [user question-id]
  (let [question-from-edn (first (filterv #(= question-id (% :question-id)) (quiz/read-question-file "question.edn")))
        a-question (quiz/show-question question-from-edn)]
    (hp/html5 (head (str "Question" (question-from-edn :question-id)))
             (body user
               [:div {:class "row"}
                    [:div {:class "col-md-4"}
                     [:h3 (str "Question #" (question-from-edn :question-id))]
                     [:form {:action "/answer-check" :method "post"}
                      [:label (a-question :problem)]
                      [:fieldset
                       [:input {:type "hidden" :name "type" :value (a-question :type)}]
                       [:input {:type "hidden" :name "q-id" :value (question-from-edn :question-id)}]
                       [:input {:type "hidden" :name "intel" :value (a-question :answer-index)}]
                       (map #(sa-or-ma-options % (a-question :type)) (a-question :answer-choices))]
                      [:button {:type "submit" :class "btn btn-success"} "Next"]]]]))))



(defn quiz-form
  []
  [:div {:class "col-md-6"}
   [:h2 "Add a Question"]
   [:form {:role "form" :action "/add-question" :method "post"}
    [:div {:class "form-group"}
     [:label {:for "problem"} "Problem: "]
     [:input {:name "problem" :type "text" :class "form-control" :placeholder "Example of problem: What is your name?"}]]
    [:div {:class "form-group"}
     [:label {:for "true-answers"} "True answer choices: "]
     [:input {:name "true-answers" :type "text" :class "form-control" :placeholder "Example of true choices: [\"Sharon\" \"Johanna\"]"}]]
    [:div {:class "form-group"}
     [:label {:for "false-answers"} "False answer choices: "]
     [:input {:name "false-answers" :type "text" :class "form-control" :placeholder "Example of false choices: [\"Toby\" \"K-CI\" \"Tata\"]"}]]
    [:button {:type "submit" :class "btn btn-success"} "Add a question"]]])

(defn sign-up
  []
  (hp/html5 (head "Pissaz - Sign Up")
            (body [:div {:class "row"}
                   [:h3 "Sign Up"]
                   [:div {:class "col-md-6"}
                    [:form {:action "/add-user" :method "post"}
                     [:div {:class "form-group"}
                      [:label {:for "username"} "Username: "]
                      [:input {:name "username" :type "text" :class "form-control" :placeholder "Enter a username"}]]
                     [:div {:class "form-group"}
                      [:label {:for "email"} "Email: "]
                      [:input {:name "email" :type "text" :class "form-control" :placeholder "Enter your email"}]]
                     [:div {:class "form-group"}
                      [:label {:for "password"} "Password: "]
                      [:input {:name "password" :type "text" :class "form-control" :placeholder "Enter your password"}]]
                     [:div {:class "form-group"}
                      [:label {:for "password-confirmation"} "Repeat Password: "]
                      [:input {:name "password-confirmation" :type "text" :class "form-control" :placeholder "Repeat your password"}]]
                     [:button {:type "submit" :class "btn btn-success"} "Register your account"]]]])))

(defn sign-in
  []
  (hp/html5 (head "Pissaz - Sign In")
            (body [:div {:class "row"}
                   [:h3 "Sign In"]
                   [:div {:class "col-md-6"}
                    [:form {:action "/add-session" :method "post"}
                     [:div {:class "form-group"}
                      [:label {:for "username"} "Username: "]
                      [:input {:name "username" :type "text" :class "form-control" :placeholder "Enter username"}]]
                     [:div {:class "form-group"}
                      [:label {:for "password"} "Password: "]
                      [:input {:name "password" :type "text" :class "form-control" :placeholder "password here"}]]
                     [:button {:type "submit" :class "btn btn-success"} "Sign In"]]]])))
