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
  (let [shuffled (shuffle (mapv first (question :answers)))
        true-answer (first (first (filter #(true? (second %)) (question :answers))))
        indexed-choices (into [] (map-indexed #(conj [%2] %1) shuffled))]
    {:problem (question :problem)
     :answer-choices shuffled
     :answer-index (second (first (filter #(= true-answer (first %)) indexed-choices)))
     }))

; Function to check the rightful answer

(defn answer-check
  [user-choice-idx answer-index]
  (= user-choice-idx answer-index))
