FROM clojure
RUN mkdir -p /usr/src/scrabble
WORKDIR /usr/src/scrabble

COPY project.clj /usr/src/scrabble

RUN lein deps
COPY . /usr/src/scrabble

RUN mv "$(lein uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" app-standalone.jar
CMD ["java", "-jar", "app-standalone.jar"]

# CMD ["lein", "run"]
