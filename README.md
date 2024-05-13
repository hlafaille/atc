# [atc](https://atc.kerosenelabs.io)

ATC; an API gateway designed from the ground up to be vendor independent.

* **Reliable:** Utilizing Java 21 and the new virtual threads, ATC will be a solid foundation for you to build reliable applications.
* **Speedy:** Speed is a must when there may be hundreds of hops between microservices within your stack before the user finally gets a response. We can handle that, and more.
* **Free and Open Source, forever:** Every cloud vendor has their own proprietary solution to API management. Here, we aim to be independent of the big guys.

## Getting Started

We recommend you use an OCI Container (Docker, Podman) to deploy ATC. Follow these basic steps (and please file any issues you come across, we're still in early development)!

1. Write your configuration file

```json
{
    "services": {
        "weather": {
            "description": "National Weather Service API",
            "maintainer": "United States Government",
            "hosts": [
                "api.weather.gov"
            ],
            "scopes": {
                "/": {
                    "methods": [
                        "GET"
                    ]
                }
            },
            "consumes": {
                "exampleApi": {
                    "/healthcheck": {
                        "methods": [
                            "GET"
                        ]
                    }
                }
            }
        },
        "exampleApi": {
            "description": "Example API provided by Kerosene Labs",
            "maintainer": "Kerosene Labs",
            "hosts": [
                "api.kerosenelabs.com"
            ],
            "scopesPrefix": "/v1",
            "scopes": {
                "/healthcheck": {
                    "methods": [
                        "GET"
                    ]
                }
            },
            "consumes": {
                "weather": {
                    "/": {
                        "methods": [
                            "GET"
                        ]
                    }
                }
            }
        }
    }
}
```

2. Pull the latest image

```bash
docker pull ghcr.io/hlafaille/atc:latest
```

3. Create a container from this image

```bash
docker run -v ${PWD}/your-config-name-here.yml:/opt/atc/configuration.yml -p 8443:8443 atc:latest
```

4. *Heyyy so not all of this works yet, we should really finish this up!*
