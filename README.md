# scrabble
[![Build Status](https://travis-ci.org/AndreaCrotti/scrabble.svg?branch=master)](https://travis-ci.org/AndreaCrotti/scrabble)

Various functions to generate list of valid words for scrabble/words with friends.

## Installation

Download from https://github.com/AndreaCrotti/scrabble

This project is composed of:

- a Clojurescript frontend using re-frame and figwheel
- a Clojure compojure backend
- a command line interface to try it out

## Testing

- *lein test* will run all the backend/API tests

## Usage

To run the CLI interface:

    $ lein run -w <word>
    
To run the frontend:

    $ lein figwheel
    
To auto compile the CSS

    $ lein garden auto

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
- see how much stuff could be moved directly in Clojurescript entirely
- check if using websockets could make more sense
- automate running of *lein garden* together with fighweel
- makes it possible to ask simpler and more specialised questions (making all the constraints and the declaration of tiles for example not a strong requirement anymore)such as:
  + all the words ending with a certain letter
  + words starting with a certain letter
