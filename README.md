# GroMoKoSo - Meta Repository

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## 🧭 Overview

<div align="center">

<img src="assets/icons/gromokoso/gromokoso-icon-transparent.png" alt="logo" width="200"/>

<p>
GroMoKoSo – in short for <strong>GroßsprachModellModellKontextProtokolServerOrchestrierung</strong> is a platform  
that enables users to integrate existing RESTful APIs and automatically create MCP Tools  
which enhance the capabilities of Large Language Models (LLMs).
</p>

</div>

## 🚀 Features

- Create and manage users, groups and Apis
- Let users add their own APIs as Tools or share APIs in groups
- Control who can add, modify or delete APIs in groups
- Add the provided MCP Server in the LLM of your choice and use the tools you have access to

## Sub repositories
![subservices.png](assets/icons/readme/subservices.png)

The project is split into multiple sub-repositories. Each repository contains one microservice
for the application. All subrepositories can be found here:  
- [Spec2Tool](https://git.thm.de/softwarearchitektur-wz-ss24/studentswa2025/enton/spec2tool)
- [UserInterface](https://git.thm.de/softwarearchitektur-wz-ss24/studentswa2025/enton/userinterface)
- [UserManagement](https://git.thm.de/softwarearchitektur-wz-ss24/studentswa2025/enton/usermanagement)
- [ApiManagament](https://git.thm.de/softwarearchitektur-wz-ss24/studentswa2025/enton/apimanagement)
- [MCPManagement](https://git.thm.de/softwarearchitektur-wz-ss24/studentswa2025/enton/mcpmanagement)

## ⚙️ Installation
1. First, clone the sub-repositories:
```bash
bash pull_subrepose.sh
```

2. Make sure docker and docker compose are installed:
```bash
docker --version
docker compose version
```

3. Start all subservices by:
```bash
docker compose up -d
```

4. To stop services, type:
```bash
docker compose down -v
```


Admin Account
Username: sysadmin
Password: 123

Client for mcp Server
Clientname: gromokoso-mcp
Clientsecret: qmvXUEaH9qxoO8PK5GdbZpfQLrWcO8hY 