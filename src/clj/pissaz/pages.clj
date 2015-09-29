(ns pissaz.pages
  (:require
    [hiccup.core :as hc]
    [hiccup.page :as hp]
    [pissaz.quiz :as quiz]))

(defn- head
  [title]
  [:head
   [:title title]
   [:meta {:charset "utf-8"}]
   [:link {:rel "stylesheet" :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"}]
   [:link {:rel "stylesheet" :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"}]
   [:link {:rel "stylesheet" :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"}]])

(defn- header
  []
  [:header
   [:ul {:class "nav nav-pills"}
    [:li {:role "presentation"}
     [:a {:href "/"} "Pissaz"]]
    [:li {:role "presentation"}
     [:a {:href "/quizzes"} "Quizzes"]]
    [:li {:role "presentation"}
     [:a {:href "/questions"} "Questions"]]]])

(defn- footer
  []
  [:div {:class "panel-footer"}
   [:div {:class "col-md-3"}
    [:a {:href "/contact"} "Contact Me"]]])

(defn- body
  [anything]
  [:body {:class "container"}
   [:div {:class "row"}
    (header)]
   anything
   [:div {:class "row"}
    (footer)]])

(defn homepage
  []
  (hp/html5 (head "Pissaz - Home")
            (body [:div {:class "row"}
                   [:div {:class "jumbotron"}
                    [:h1 "Jumbo Pic"]]
                   [:p "bla bla bla about Pissaz"]])))

(defn contact
  []
  (hp/html5 (head "Pissaz - Contact")
            (body [:div {:class "row"}
                   [:p "Call me"]])))

(defn quizzes
  []
  (hp/html5 (head "Pissaz All Quizzes")
            (body [:div {:class "row"}
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
  [id]
  "Sementara quiz 1 dulu lah yah"
  (let [the-quiz id]
    (hp/html5 (head (str "Quiz #" id))
              (body [:div {:class "row"}
                     [:div {:class "col-md-2"}
                      [:ul {:class "nav nav-pills nav-stacked"}
                       [:li {:role "presentation"}
                        [:a {:href "quiz/1"}  "Quiz 1"]]]]
                     [:div {:class "col-md-10"}
                      [:h2 "Belom ada"]]]))))


(defn answer-choice-to-html
  [a-choice]
  [:input {:type "radio" :name "1" :value (a-choice :idx)} (a-choice :string)])


(defn questions
  "to show all questions (samples)"
  []
  (let [all-questions (quiz/read-question-file "question.edn")
        question-list (fn [que] (hc/html [:li {:role "presentation"}
                                          [:a {:href (str "/question/" (que :question-id))} (str "Question nomor " (que :question-id))]]))]
    (hp/html5 (head "Pissaz All Questions")
              (body [:div {:class "row"}
                     [:div {:class "col-md-2"}
                      [:ul {:class "nav nav-pills nav-stacked"}
                       (map question-list all-questions)]]
                     [:div {:class "col-md-10"}
                      [:h3 "All Quizzes Broh"]
                      [:button {:type "submit" :class "btn btn-success"}
                       "Add a quiz"]
                      [:br]]]))))

(defn question
  "to show question (sample)"
  [question-id]
  (let [question-from-edn (first (filterv #(= question-id (% :question-id)) (quiz/read-question-file "question.edn")))
        a-question (quiz/show-question question-from-edn)]
    (hp/html5 (head (str "Question" (question-from-edn :question-id)))
             (body
               [:div {:class "row"}
                    [:div {:class "col-md-4"}
                     [:h3 (str "Question #" (question-from-edn :question-id))]
                     [:form {:action "/answer-check" :method "post"}
                      [:label (a-question :problem)]
                      [:fieldset
                       [:input {:type "hidden" :name "intel" :value (a-question :answer-index)}]
                       (map answer-choice-to-html (a-question :answer-choices))]
                      [:button {:type "submit" :class "btn btn-success"} "Next"]]]]))))