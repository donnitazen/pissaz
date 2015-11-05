(ns pissaz.dbase
  (:require
    [com.stuartsierra.component :as component]))

(defrecord Database [quiz question user]
  component/Lifecycle
  (start [this]
    this)
  (stop [this]
    this))

(defn reading-file
  [path-to-file]
  (read-string (slurp path-to-file)))

(defn create
  [quiz question user]
  (map->Database {:question-db (reading-file question)
                  :quiz-db (reading-file quiz)
                  :user-db (reading-file user)}))


