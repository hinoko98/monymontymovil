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
