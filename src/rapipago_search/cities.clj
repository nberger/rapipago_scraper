(ns rapipago-search.cities
  (require [net.cgrand.enlive-html :as html]
           [clj-http.client :as http]
           [rapipago-search.util :refer :all]))

(defn- fetch-cities-options
  [province]
  (let [url "http://www.rapipago.com.ar/rapipagoWeb/consultar-ciudades.htm"
        province-id (:id province)]
    (:body (http/get url
                     {:query-params {:provinciaId province-id}}))))

(comment
  (fetch-cities-options (second (provinces)))
)

(defn- cities-options
  [province]
  (let [reader (java.io.StringReader. (fetch-cities-options province))
        options-html (html/html-resource reader)]
    (html/select options-html [:option])))

(comment
  (cities-options (second (provinces)))
)

(defn cities
  "List of cities in a province"
  [province]
  (map parse-option
       (filter is-option-present? (cities-options province))))

(comment
  (cities (second (provinces)))
)
