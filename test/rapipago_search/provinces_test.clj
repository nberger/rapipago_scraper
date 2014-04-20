(ns rapipago-search.provinces-test
  (:require [clojure.test :refer :all]
            [rapipago-search.provinces :refer :all]))

(def buenos-aires {:name "Buenos Aires" :id 28})

(defn stub-provinces-all
  []
  (list {:name "Cordoba" :id 1} {:name "Salta" :id 2} buenos-aires))

(deftest province-by-name
  (testing "find a province given the name"
    (with-redefs [all stub-provinces-all]
      (is (= [buenos-aires] (find-by-name "Buenos Aires")))
      (is (= [buenos-aires] (find-by-name "buenos aires"))))))
