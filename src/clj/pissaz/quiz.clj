(ns pissaz.quiz)

(def question1
  {:problem "Stark?"
   :answers [["Theon" false]
             ["Sansa" true]
             ["Cersei" false]
             ["Samwell" false]]})

; Function to show the question

(defn show-question
  [question]
  {:problem (question :problem)
   :answer_choices (shuffle (mapv first (question :answers)))
   })

; Function to check the rightful answer

(defn answer-check
  [question user-choice-idx answer-choices]
  (let [answer-list (question :answers)
        user-choice (answer-choices user-choice-idx)]
    (->> answer-list
         (filter #(= (first %) user-choice))
         (first)
         (second))))
