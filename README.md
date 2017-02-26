# scrabble
[![Build Status](https://travis-ci.org/AndreaCrotti/scrabble.svg?branch=master)](https://travis-ci.org/AndreaCrotti/scrabble)

Various functions to generate list of valid words for scrabble/words with friends.

## Installation

Download from https://github.com/AndreaCrotti/scrabble

## Usage

    $ lein run -w <word>

## Examples

    $ lein run -w friend
    ({:value 11, :word finder} {:value 11, :word friend})

## License

Copyright © 2017 Andrea Crotti

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

## Other ideas

- add authentication to store games and preferences (JWT for example)
- add dictionaries for other languages as well
- use transit instead of json to communicate between the API and the frontend
- run also clojurescript in Travis
- move common code into cljc
