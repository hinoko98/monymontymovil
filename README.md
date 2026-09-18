# MonyMonty Mobile

Aplicación móvil de **MonyMonty** para la gestión de finanzas personales, desarrollada para **Android e iOS** utilizando **Flutter**.

MonyMonty busca ofrecer una experiencia sencilla para que las personas puedan registrar, organizar y consultar su información financiera desde cualquier lugar.

## Plataformas

* Android
* iOS

## Tecnologías

* **Flutter**
* **Dart**
* Android
* iOS
* API REST de MonyMonty

## Arquitectura

La aplicación móvil funciona como cliente de la plataforma MonyMonty y consume los servicios proporcionados por su API.

```text
┌──────────────────────┐
│    MonyMonty Mobile  │
│                      │
│       Flutter        │
│         Dart         │
└──────────┬───────────┘
           │
           │ API REST
           ▼
┌──────────────────────┐
│    MonyMonty API     │
│                      │
│    Node.js/Express   │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│       MongoDB        │
└──────────────────────┘
```

La aplicación móvil **no contiene directamente la lógica de persistencia del sistema**. La comunicación con los datos y servicios de MonyMonty se realiza mediante la API.


## 🏢 MonyMonty

**MonyMonty** es un producto de **MonteFlor**, orientado a facilitar la gestión de las finanzas personales.

🌐 [monymonty.monteflor.co](https://monymonty.monteflor.co/)

---

> Este repositorio contiene exclusivamente el cliente móvil de MonyMonty. Los servicios de backend y otros productos de MonteFlor se mantienen en repositorios independientes.


## Únete a Nuestra Comunidad

Con MonyMonty, estamos construyendo una comunidad comprometida en capacitar a las personas para alcanzar una salud financiera óptima. ¡Nos
encantaría que te unas a nosotros y comiences a tomar el control de tu futuro financiero hoy mismo!

## Conéctate con Nosotros

Para obtener más información sobre el proyecto, visita nuestras redes sociales y mantente al tanto de las últimas novedades:

<p align="left">

</p>

## Conéctate con el creador

<p align="left"> 
    <a href="https://discord.com/users/Oug#6073" target="_blank" rel="noreferrer">
        <img src="https://raw.githubusercontent.com/danielcranney/readme-generator/main/public/icons/socials/discord.svg" width="32" height="32" />
    </a>
    <a href="https://www.github.com/OugMontiel" target="_blank" rel="noreferrer">
        <img src="https://raw.githubusercontent.com/danielcranney/readme-generator/main/public/icons/socials/github.svg" width="32" height="32" />
    </a>
    <a href="http://www.instagram.com/oug_montiel/" target="_blank" rel="noreferrer">
        <img src="https://raw.githubusercontent.com/danielcranney/readme-generator/main/public/icons/socials/instagram.svg" width="32" height="32" />
    </a>
    <a href="https://www.linkedin.com/in/diego-alejandro-montiel-florez-data-science/" target="_blank" rel="noreferrer">
        <img src="https://raw.githubusercontent.com/danielcranney/readme-generator/main/public/icons/socials/linkedin.svg" width="32" height="32" />
    </a>
</p>

## License

This project is licensed under the GNU Affero General Public License v3.0 (AGPL-3.0).