# Sistema de Monitoramento de Rede

Este projeto implementa um sistema completo de monitoramento de rede utilizando Spring Boot, React, Prometheus, Grafana e RabbitMQ.

## Arquitetura

O sistema é composto por:

- **Backend (Spring Boot)**
  - Monitoramento de rede em tempo real
  - Exposição de métricas para Prometheus
  - Integração com RabbitMQ para eventos
  - API REST para consulta de status

- **Frontend (React)**
  - Dashboard interativo
  - Visualização em tempo real
  - Gráficos de latência e perda de pacotes
  - Interface responsiva

- **Monitoramento (Prometheus + Grafana)**
  - Coleta de métricas
  - Visualização de dados históricos
  - Dashboards personalizados
  - Alertas configuráveis

- **Mensageria (RabbitMQ)**
  - Processamento assíncrono de eventos
  - Fila de mensagens para escalabilidade
  - Interface de administração

## Requisitos

- Docker
- Docker Compose
- Java 17 (para desenvolvimento)
- Node.js 18+ (para desenvolvimento)

## Configuração e Execução

1. **Clone o repositório**
```bash
git clone <repository-url>
cd monitoramento-rede
```

2. **Build do projeto**
```bash
# Build do backend
cd backend
./mvnw clean package

# Build do frontend
cd ../frontend
npm install
npm run build
```

3. **Iniciar os serviços**
```bash
docker-compose up --build
```

## Acessando os Serviços

- **Frontend**: http://localhost
  - Dashboard principal do sistema

- **Backend API**: http://localhost:8080
  - `/api/network/status` - Status geral
  - `/api/network/status/{destination}` - Status específico
  - `/api/network/targets` - Alvos monitorados
  - `/actuator/prometheus` - Métricas Prometheus

- **Prometheus**: http://localhost:9090
  - Interface do Prometheus
  - Consulta de métricas
  - Status dos targets

- **Grafana**: http://localhost:3000
  - Login: admin
  - Senha: admin
  - Dashboards pré-configurados
  - Visualização de métricas

- **RabbitMQ Management**: http://localhost:15672
  - Login: guest
  - Senha: guest
  - Gerenciamento de filas
  - Monitoramento de mensagens

## Desenvolvimento

### Backend (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run
```

### Frontend (React)

```bash
cd frontend
npm install
npm run dev
```

## Monitoramento

O sistema monitora:
- Latência de rede
- Perda de pacotes
- Disponibilidade
- Métricas de sistema

## Contribuição

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Crie um Pull Request