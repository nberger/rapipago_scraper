(ns rapipago-scraper.cities
  (require [net.cgrand.enlive-html :as html]
           [clj-http.client :as http]
           [rapipago-scraper.util :refer :all]
           [rapipago-scraper.provinces :as provinces]))

(defn- fetch-cities-options
  [{province-id :id}]
  (let [url "http://www.rapipago.com.ar/rapipagoWeb/consultar-ciudades.htm"]
    (:body (http/get url
                     {:query-params {:provinciaId province-id}}))))

(comment
  (def salta (first (provinces/by-name "SALTA")))
  (fetch-cities-options salta)
)

(defn- cities-options
  [province]
  (-> province
      fetch-cities-options
      html/html-snippet
      (html/select [:option])))

(comment

  (cities-options salta)

)

(defn find-in-province
  "Finds the cities in a province"
  [province]
  (map parse-option
       (rest (cities-options province))))

(comment

  (find-in-province salta)

)
