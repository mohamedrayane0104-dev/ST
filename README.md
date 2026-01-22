# 🎯 SkillTrack

SkillTrack est une application web full-stack permettant la gestion des utilisateurs, de leurs profils et de leurs points de compétence.  
Elle intègre un système d’authentification sécurisé par JWT, la modification de profil, ainsi que la suppression de compte avec confirmation par e-mail.

---

## 🚀 Fonctionnalités

- Inscription avec confirmation par e-mail  
- Connexion / Déconnexion sécurisée avec JWT  
- Consultation du profil utilisateur  
- Suppression du compte avec e-mail de confirmation  
- Gestion des points et du niveau utilisateur (en progrès)  

---

## 🛠️ Technologies utilisées

### Backend
- Java 17  
- Spring Boot  
- Spring Security  
- JWT Authentication  
- JPA / Hibernate  
- PostgreSQL  
- Mailtrap (tests e-mail)

### Frontend
- React 18  
- TypeScript  
- Axios  
- React Router  
- CSS personnalisé (Responsive)

---

## 📂 Structure du projet

skilltrack/
│
├── backend/
│ └── src/main/java/com/skilltrack/backend/
│ ├── auth/
│ │ ├── controller/
│ │ ├── service/
│ │ └── jwt/
│ ├── config/
│ ├── model/
│ └── repository/
│
└── frontend/
└── src/
├── pages/
├── context/
├── components/
└── App.tsx

---

## ⚙️ Installation

### 1️⃣ Backend

#### Prérequis
- Docker Desktop
- Java 17+
- Maven
- PostgreSQL

#### Étapes

```bash
git clone https://github.com/TON-USERNAME/skilltrack.git
cd skilltrack
docker compose up --build
```
🔐 Authentification JWT

Le token JWT est stocké dans localStorage

Chaque requête protégée envoie :

Authorization: Bearer <token>


Déconnexion invalide le token côté serveur

✉️ Vérification e-mail

À l’inscription, un mail est envoyé via Mailtrap

L’utilisateur clique sur le lien pour activer son compte

Idem pour la suppression du compte


🧪 Tests


Postman pour tester les endpoints

Mailtrap pour visualiser les e-mails

Console navigateur pour vérifier JWT

📸 Aperçu

Page Login / Register

Page Profil

Édition du profil

Confirmation e-mail

👨‍💻 Auteur

Mohamed Rayane Costo




