(ns pissaz.quiz)

; Function to read from edn

(defn read-question-file
  [filename]
  (read-string (slurp (str "resources/data/" filename))))


; Function to show the question

(defn show-question
  [question]
  (let [shuffled (map-indexed #(conj %2 %1) (shuffle (question :answers)))
        true-answer ((first (filterv second shuffled)) 2)]
      {:problem (:problem question)
       :answer-choices (mapv #(assoc {} :idx (last %) :string (first %)) shuffled)
       :answer-index true-answer}))
        

; Function to check the rightful answer

(defn answer-check
  [user-choice-idx answer-index]
  (= user-choice-idx answer-index))

; Function to add a question