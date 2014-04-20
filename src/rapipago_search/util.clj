(ns rapipago-search.util)

(defn is-option-empty?
  [option]
  (let [{{value :value} :attrs} option]
    (empty? value)))

(defn parse-option
  [option]
  (let [{content :content {value :value} :attrs} option]
    {:name (first content) :id value}))

(comment
  (parse-option {:content '("Buenos Aires") :attrs {:value "1"}})
)

