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

(defn add-question
  [q-map]
  (let [all-questions (read-question-file "question.edn")
        true-answers (mapv #(vector % true) (read-string (q-map :true-answers)))
        false-answers (mapv #(vector % false) (read-string (q-map :false-answers)))
        q-id (str (inc (read-string ((last all-questions) :question-id))))
        new-answers (vec (concat true-answers false-answers))
        question-with-answers (merge q-map {:question-id q-id :answers new-answers})
        new-question (dissoc question-with-answers :true-answers :false-answers)]
    (spit "resources/data/question.edn" (conj all-questions new-question))
    q-id))