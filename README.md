<div align="center">
  <img src="https://imgur.com/3jwkDd1.png" alt="blue-book"/>
  <h1>Hyro's Planet</h1>
  <a href="https://discord.gg/5yVnrtRCGb" target="_blank"><img height=20 src="https://img.shields.io/discord/1193186539212128306" /></a>
  <img src="https://img.shields.io/github/stars/Mardroide/megatron" alt="stars">
</div>

### [Visit the website](https://hyro.one)

Megatron is an ecosystem of plugins and libraries that help manage a minecraft network designed specifically for a single server. Development, compilation, be very fast!

## Documentation

- [Core](#core)
- [Lib](#lib)
- [Proxy](#proxy)

## How to start

1. Clone the repository

```console
git clone https://github.com/Hyro32/hyros-planet.git
```

2. Build the project

```console
mvn build
```

## Velocity with Docker

All paper servers are exposed only within the docker network, not externally. So you can't access them outside of that docker compose file.

To bind the server to the velocity proxy, follow the example bellow:

```yml
[servers]
# Configure your servers here. Each key represents the server's name, and the value
# represents the IP address of the server to connect to.
paper-1 = 'paper-1'
```

## Chat with us

Learn more about the project and the progress of its development is joining the [discord community](https://discord.gg/5yVnrtRCGb).

## Contribute

Are you a developer passionate about Minecraft and the community? If so, we invite you to contribute to our project for an open source Minecraft network that prioritizes transparency and security.

In this project, collaboration is key. Being open source, the network's code is available for everyone to inspect, ensuring no cheating or backdoors. In addition, the community can work together to identify and fix vulnerabilities, creating a more secure environment for everyone.

Contributing to this project not only allows you to improve the security and transparency of the network, but also gives you the opportunity to participate in a vibrant community of players and developers who share your passion for Minecraft. You can contribute your talents in different areas, such as code development, documentation creation, testing or translation.

No matter your level of experience, there is something for everyone! Join us and help us create a more transparent, secure and innovative Minecraft network. We look forward to your collaboration!

> Want to make a larger contribution? Please see [CONTRIBUTING.md](/CONTRIBUTING.md) first!

## Technologies

- [Paper](https://papermc.io/)
- [Velocity](https://papermc.io/software/velocity)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)
- [Kotlin](https://kotlinlang.org/)
