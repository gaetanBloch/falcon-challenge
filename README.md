[![Falcon Challenge CI](https://github.com/gaetanBloch/falcon-challenge/actions/workflows/workflow.yml/badge.svg)](https://github.com/gaetanBloch/falcon-challenge/actions)
[![codecov](https://codecov.io/gh/gaetanBloch/falcon-challenge/branch/main/graph/badge.svg?token=FzhPJc8wa3)](https://codecov.io/gh/gaetanBloch/falcon-challenge)

# Millenium Falcon Challenge

## What are the odds?

The Death Star - the Empire's ultimate weapon - is almost operational and is currently approaching the Endor planet. The countdown has started.

Han Solo, Chewbacca, Leia and C3PO are currently on Tatooine boarding on the Millennium Falcon. They must reach Endor to join the Rebel fleet and destroy the Death Star before it annihilates the planet.

The Empire has hired the best bounty hunters in the galaxy to capture the Millennium Falcon and stop it from joining the rebel fleet...

### Routes
The Millennium Falcon has an onboard map containing the list of all planets in the galaxy, and the number of days it takes to go from one to the other using an Hyperspace jump.

### Autonomy
However, the Millennium Falcon is not the newest ship of its kind and it has a limited autonomy. If it's lacking fuel to achieve his next Hyperspace jump, it first must stop for 1 day on the nearby planet to refuel.
For example, if its autonomy is 6 days, and it has already done a 4 days Hyperspace jump. It can reach another planet that is 1 or 2 days away from its current position. To reach planets that are 3 days or more away, it must refuel first.

### Bounty Hunters
The rebel command center has also intercepted a communication and the rebels know the various locations and days where Bounty Hunter have planned to hunt the Millennium Falcon.
If the Millennium Falcon arrives on a planet on a day where Bounty Hunter are expected to be on this planet, the crew has 10% chance of being captured. Likewise, if the Millennium Falcon refuels on a planet on a day where Bounty Hunter are expected to be on this planet, the crew has 10% chance of being captured.

Hint: To avoid the Bounty Hunters, the Millennium Falcon can land on a planet with no bounty hunters (even if its tank is full) and wait for 1 or more days before pursuing its route.

The mathematical formula to compute the total probability of being captured is:

$$ {1 \over 10} + { 9 \over 10^2 } + { 9^2 \over 10^3 } + ... + { 9^k \over 10^{k+1} } $$


where k is the number of times the Bounty Hunter tried to capture the Millennium Falcon.

For example, the probability to get captured is:
   - if the Millennium Falcon travels via 1 planet with bounty hunters:
  
$$ {1 \over 10} = 0.1 $$
   
   - if the Millennium Falcon travels via 1 planet with bounty hunters and refuels on this planet:

$$ {1 \over 10} + { 9 \over 10^2 } = 0.19 $$

   - if the Millennium Falcon travels via 2 planets with bounty hunters:

$$ {1 \over 10} + { 9 \over 10^2 } = 0.19 $$

   - if the Millennium Falcon travels via 3 planets with bounty hunters:

$$ {1 \over 10} + { 9 \over 10^2 } + { 9^2 \over 10^3 } = 0.271 $$

## Application

The goal of the application is to compute and display the odds that the Millennium Falcon reaches Endor in time and saves the galaxy.

![Never tell me the odds](resources/never-tell-me-the-odds.gif)

The web application will be composed of a backend (the Millennium Falcon onboard computer), a front-end and a CLI (command-line interface).

### Back-end

When it starts, the back-end service will read a JSON configuration file containing the autonomy, the path towards an SQLite database file containing all the routes, the name of the planet where the Millennium Falcon is currently parked (Tatooine) and the name of the planet that the empire wants to destroy (Endor).

**millennium-falcon.json**
```json
{
  "autonomy": 6,
  "departure": "Tatooine",
  "arrival": "Endor",
  "routes_db": "universe.db"
}
```
   - autonomy (integer): autonomy of the Millennium Falcon in days.
   - departure (string): Planet where the Millennium Falcon is on day 0.
   - arrival (string): Planet where the Millennium Falcon must be at or before countdown.
   - routes_db (string): Path toward a SQLite database file containing the routes. The path can be either absolute or relative to the location of the `millennium-falcon.json` file itself.

The SQLite database will contain a table named ROUTES. Each row in the table represents a space route. Routes can be travelled **in any direction** (from origin to destination or vice-versa).

   - ORIGIN (TEXT): Name of the origin planet. Cannot be null or empty.
   - DESTINATION (TEXT): Name of the destination planet. Cannot be null or empty.
   - TRAVEL_TIME (INTEGER): Number days needed to travel from one planet to the other. Must be strictly positive.

| ORIGIN   | DESTINATION | TRAVEL_TIME |
|----------|-------------|-------------|
| Tatooine | Dagobah     | 4           |
| Dagobah  | Endor       | 1           |

### Front-end

The front-end should consists of a single-page application offering users a way to upload a JSON file containing the data intercepted by the rebels about the plans of the Empire and displaying the odds (as a percentage) that the Millennium Falcon reaches Endor in time and saves the galaxy.

**empire.json**
```json
{
  "countdown": 6, 
  "bounty_hunters": [
    {"planet": "Tatooine", "day": 4 },
    {"planet": "Dagobah", "day": 5 }
  ]
}
```

   - countdown (integer): number of days before the Death Star annihilates Endor
   - bounty_hunters (list): list of all locations where Bounty Hunter are scheduled to be present.
      - planet (string): Name of the planet. It cannot be null or empty.
      - day (integer): Day the bounty hunters are on the planet. 0 represents the first day of the mission, i.e. today.


The web page will display the probability of success as a number ranging from 0 to 100%:
- `0%` if the Millennium Falcon cannot reach Endor before the Death Star annihilates Endor
- `x% (0 < x < 100)` if the Millennium Falcon can reach Endor before the Death Star annihilates Endor but might be captured by bounty hunters.
- `100%` if the Millennium Falcon can reach Endor before the Death Star annihilates Endor without crossing a planet with bounty hunters on it.

### CLI

The command-line interface should consist of an executable that takes 2 files paths as input (respectively the paths toward the `millennium-falcon.json` and `empire.json` files) and prints the probability of success as a number ranging from 0 to 100.
```sh
$ give-me-the-odds example1/millennium-falcon.json example1/empire.json
81
```

## Examples

### Example 1
**[universe.db](examples/example1/universe.db?raw=true)** (click to download)
| ORIGIN   | DESTINATION | TRAVEL_TIME |
|----------|-------------|-------------|
| Tatooine | Dagobah     | 6           |
| Dagobah  | Endor       | 4           |
| Dagobah  | Hoth        | 1           |
| Hoth     | Endor       | 1           |
| Tatooine | Hoth        | 6           |

**[millennium-falcon.json](examples/example1/millennium-falcon.json?raw=true)** (click to download)
```
{
  "autonomy": 6,
  "departure": "Tatooine",
  "arrival": "Endor",
  "routes_db": "universe.db"
}
```
**[empire.json](examples/example1/empire.json?raw=true)** (click to download)
```
{
  "countdown": 7, 
  "bounty_hunters": [
    {"planet": "Hoth", "day": 6 }, 
    {"planet": "Hoth", "day": 7 },
    {"planet": "Hoth", "day": 8 }
  ]
}
```

The application should display 0% as The Millennium Falcon cannot go from Tatooine to Endor in 7 days or less (the Millennium Falcon must refuel for 1 day on either Dagobah or Hoth).

### Example 2
**[universe.db](examples/example2/universe.db?raw=true)** same as above

**[millennium-falcon.json](examples/example2/millennium-falcon.json?raw=true)**: same as above

**[empire.json](examples/example2/empire.json?raw=true)** (click to download)
```
{
  "countdown": 8, 
  "bounty_hunters": [
    {"planet": "Hoth", "day": 6 }, 
    {"planet": "Hoth", "day": 7 },
    {"planet": "Hoth", "day": 8 }
  ]
}
```

The application should display 81% as The Millennium Falcon can go from Tatooine to Endor in 8 days with the following plan:
- Travel from Tatooine to Hoth, with 10% chance of being captured on day 6 on Hoth.
- Refuel on Hoth with 10% chance of being captured on day 7 on Hoth.
- Travel from Hoth to Endor

### Example 3
**[universe.db](examples/example3/universe.db?raw=true)** same as above

**[millennium-falcon.json](examples/example3/millennium-falcon.json?raw=true)**: same as above

**[empire.json](examples/example3/empire.json?raw=true)**
```
{
  "countdown": 9, 
  "bounty_hunters": [
    {"planet": "Hoth", "day": 6 }, 
    {"planet": "Hoth", "day": 7 },
    {"planet": "Hoth", "day": 8 }
  ]
}
```

The application should display 90% as The Millennium Falcon can go from Tatooine to Endor in 9 days with the following plan:
- Travel from Tatooine to Dagobath.
- Refuel on Dagobah
- Travel from Dagobah to Hoth, with 10% chance of being captured on day 8 on Hoth.
- Travel from Hoth to Endor

### Example 4
**[universe.db](examples/example4/universe.db?raw=true)** same as above

**[millennium-falcon.json](examples/example4/millennium-falcon.json?raw=true)**: same as above

**[empire.json](examples/example4/empire.json?raw=true)**
```
{
  "countdown": 10, 
  "bounty_hunters": [
    {"planet": "Hoth", "day": 6 }, 
    {"planet": "Hoth", "day": 7 },
    {"planet": "Hoth", "day": 8 }
  ]
}
```

The application should display 100% as The Millennium Falcon can go from Tatooine to Endor in 10 days and avoid Bounty Hunters with the following plans:
- Travel from Tatooine to Dagobah,  refuel on Dagobah
- Wait for 1 day on Dagobah
- Travel from Dagobah to Hoth
- Travel from Hoth to Endor

or

- Wait for 1 day on Tatooine
- Travel from Tatooine to Dagobah, refuel on Dagobah
- Travel from Dagobah to Hoth
- Travel from Hoth to Endor

### Prerequisites

- Install [Docker](https://www.docker.com/)

If you need to run locally the project:
- Install [Java 17+](https://jdk.java.net/archive/)
- Download latest release of the project [v0.1.0](https://github.com/gaetanBloch/falcon-challenge/releases/download/v0.1.0/falcon-challenge-0.1.0.tar.gz) with:

```sh
wget https://github.com/gaetanBloch/falcon-challenge/releases/download/v0.1.0/falcon-challenge-0.1.0.tar.gz
```

or

```sh
curl -L https://github.com/gaetanBloch/falcon-challenge/releases/download/v0.1.0/falcon-challenge-0.1.0.tar.gz
```

### Web Application

#### To run the web application in a docker container from the Github container registry:

```sh
docker pull ghcr.io/gaetanbloch/falconchallenge:latest
docker run -i --rm -p 8080:8080 ghcr.io/gaetanbloch/falconchallenge:latest
```

#### To run the web application locally:

```sh
tar -xzf falcon-chalenge-0.1.0.tar.gz
cd falcon-chalenge-0.1.0/web
./falcon-chalenge-0.1.0.sh
```

#### To run the web application in a docker container after building the image locally:

```sh
git clone https://github.com/gaetanBloch/falcon-challenge.git
cd falcon-challenge
./run-web-container.sh
```

Open your browser on [http://localhost:8080](http://localhost:8080), you should have a web page looking like this:

![UI](resources/ui.png)

You can also access to the swagger documentation page on [swagger-ui](http://localhost:8080/q/swagger-ui), you should have a web page looking like this:

![UI](resources/swagger-ui.png)

### CLI Application

To run the cli application:

```sh
cd falcon-challenge-0.1.0/cli
./give-me-the-odds.sh <falconConfigFilePath> <empireFilePath>
```

You can see the usage of the commande by typing:

```sh
./give-me-the-odds.sh --help
Usage: give-me-the-odds [-hV] <falconConfigFilePath> <empireFilePath>
Get the odds of reaching the destination planet
      <falconConfigFilePath>
                         The abslute or relative path to the Millenium Falcon's
                           config file
      <empireFilePath>   The absolute or relative path to the Intercepted
                           Empire file
  -h, --help             Show this help message and exit.
  -V, --version          Print version information and exit.
```

## Repository

To build the project without tests:

```sh
git clone https://github.com/gaetanBloch/falcon-challenge.git
cd falcon-challenge
./mvnw clean package -DskipTests
```

To run the tests of the project:

```sh
git clone https://github.com/gaetanBloch/falcon-challenge.git
cd falcon-challenge
./mvnw clean test
```

To run the project in dev mode:

```sh
git clone https://github.com/gaetanBloch/falcon-challenge.git
cd falcon-challenge
./mvnw
```

To change the *millenium-falcon.json* and execute the web application:

```sh
git clone https://github.com/gaetanBloch/falcon-challenge.git
cd falcon-challenge
vim web/src/main/resources/millenium-falcon.json
./mvnw clean package
java -jar web/target/falcon-challenge-0.1.1-SNAPSHOT-runner.jar
```
