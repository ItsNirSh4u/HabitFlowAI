# HabitFlow Unified Backend

Complete backend API for HabitFlow AI with authentication, database integration, and AI-powered features.

## Features

- **Authentication**: User registration and login with JWT tokens
- **Database**: MongoDB integration with User and Habit models
- **AI Integration**: Support for both OpenAI and Google Gemini APIs
- **Persona System**: AI-powered persona determination and management
- **Motivation Generation**: Personalized motivation messages
- **Habit Recommendations**: AI-powered habit suggestions

## Setup

1. Install dependencies:
```bash
npm install
```

2. Create `.env` file with required environment variables (see `.env.example`)

3. Start development server:
```bash
npm run dev
```

4. Build for production:
```bash
npm run build
npm start
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Personas
- `GET /api/persona/all` - Get all available personas
- `POST /api/persona/determine` - Determine user persona based on questionnaire

### AI Features
- `POST /api/motivation/generate` - Generate personalized motivation message
- `POST /api/habits/recommendations` - Get habit recommendations
- `POST /api/classify` - AI-powered persona classification (protected)

### Utility
- `GET /api/health` - Health check endpoint
- `GET /` - API documentation

## Environment Variables

See `.env.example` for required configuration.
