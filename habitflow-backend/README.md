# HabitFlow Backend API

TypeScript backend service for HabitFlow Android app with AI integration.

## Features

- 🤖 AI-powered motivation messages (OpenAI integration)
- 👤 Persona determination (Supporter & Challenger)
- 📊 Habit recommendations
- 🔄 RESTful API for Android client
- 🎯 TypeScript for type safety

## Setup

1. Install dependencies:
```bash
npm install
```

2. Configure environment variables:
```bash
cp .env.example .env
# Edit .env and add your OpenAI API key (optional)
```

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

### Health Check
```
GET /api/health
```

### Get All Personas
```
GET /api/persona/all
```

### Determine Persona
```
POST /api/persona/determine
Body: {
  "answers": [
    { "questionId": "q1", "answer": "A" }
  ]
}
```

### Generate Motivation
```
POST /api/motivation/generate
Body: {
  "personaId": "SUPPORTER",
  "habitsCompleted": 2,
  "totalHabits": 3,
  "context": "morning routine"
}
```

### Get Habit Recommendations
```
POST /api/habits/recommendations
Body: {
  "personaId": "CHALLENGER",
  "completedHabits": ["Drink water", "Exercise"],
  "context": "need more challenges"
}
```

## Tech Stack

- Express.js
- TypeScript
- OpenAI API (optional)
- Axios for HTTP requests
- dotenv for configuration
- CORS enabled for mobile app

## Environment Variables

- `PORT` - Server port (default: 3000)
- `OPENAI_API_KEY` - OpenAI API key (optional, uses mock if not provided)
- `NODE_ENV` - Environment mode (development/production)

