# HabitFlow Unified Backend - Integration Guide

## Overview

The unified backend successfully merges features from both `backend/` and `habitflow-backend/` directories:

### Features from `backend/`
✅ User authentication (register/login) with MongoDB
✅ JWT token-based security
✅ User and Habit models with Mongoose
✅ Gemini AI for advanced persona classification
✅ Protected routes with authentication middleware

### Features from `habitflow-backend/`
✅ Persona determination system (Supporter/Challenger)
✅ AI-powered motivation message generation
✅ Habit recommendations
✅ OpenAI integration for personalized content
✅ Health check and API documentation endpoints

## API Endpoints

### Authentication (MongoDB-backed)
- `POST /api/auth/register` - Register new user
  - Body: `{ email, password }`
  - Returns: `{ token, user: { id, email, persona } }`

- `POST /api/auth/login` - Login existing user
  - Body: `{ email, password }`
  - Returns: `{ token, user: { id, email, persona } }`

### Persona Management
- `GET /api/persona/all` - Get all available personas
  - Returns: `{ success, personas: [...] }`

- `POST /api/persona/determine` - Determine persona from questionnaire
  - Body: `{ answers: [{ questionId, answer }] }`
  - Returns: `{ success, persona: {...} }`

### AI Features
- `POST /api/motivation/generate` - Generate personalized motivation
  - Body: `{ personaId, context?, habitsCompleted?, totalHabits? }`
  - Returns: `{ success, motivation: { message, personaId, timestamp } }`

- `POST /api/habits/recommendations` - Get habit recommendations
  - Body: `{ personaId, completedHabits: [], context? }`
  - Returns: `{ success, recommendations: [...] }`

- `POST /api/classify` - AI persona classification (Gemini) 🔒 Protected
  - Headers: `Authorization: Bearer <token>`
  - Body: `{ quizAnswers, goalText }`
  - Returns: `{ persona, reasoning, theme_config: { color } }`

### Utility
- `GET /api/health` - Health check
- `GET /` - API documentation

## Android App Integration

The Android app is **already compatible** with the unified backend! 

### Current Configuration
The Android app uses Retrofit with the following structure:
- **ApiClient**: Configures Retrofit with base URL from `BuildConfig.API_BASE_URL`
- **HabitFlowApiService**: Defines all API endpoints

### Setting Up the Connection

1. **Configure the backend URL** in your Android project's `local.properties` or `build.gradle.kts`:
   ```kotlin
   buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:3000/\"")
   ```
   
   Note: Use `10.0.2.2` for Android emulator or your computer's IP address for physical devices

2. **Start the unified backend**:
   ```bash
   cd unified-backend
   npm install
   cp .env.example .env
   # Edit .env with your configuration
   npm run dev
   ```

3. **Backend will run on** `http://localhost:3000`

### API Compatibility Matrix

| Android Endpoint | Unified Backend | Status |
|------------------|-----------------|--------|
| `/api/health` | ✅ | Compatible |
| `/api/persona/determine` | ✅ | Compatible |
| `/api/persona/all` | ✅ | Compatible |
| `/api/motivation/generate` | ✅ | Compatible |
| `/api/habits/recommendations` | ✅ | Compatible |

**All existing Android API calls will work without modification!**

## Environment Setup

### Required Environment Variables

Create a `.env` file in the `unified-backend/` directory:

```env
# Server
PORT=3000

# Database (Required for authentication)
MONGO_URI=mongodb://localhost:27017/habitflow

# Security (Required for JWT)
JWT_SECRET=your-secure-random-string-here

# AI Integration (Optional)
OPENAI_API_KEY=sk-...
GEMINI_API_KEY=...
```

### Quick Start

1. **Install dependencies**:
   ```bash
   cd unified-backend
   npm install
   ```

2. **Configure environment**:
   ```bash
   cp .env.example .env
   # Edit .env with your settings
   ```

3. **Run in development mode**:
   ```bash
   npm run dev
   ```

4. **Build for production**:
   ```bash
   npm run build
   npm start
   ```

## Testing the Integration

### Test the backend:
```bash
# Health check
curl http://localhost:3000/api/health

# Get all personas
curl http://localhost:3000/api/persona/all

# Register a user
curl -X POST http://localhost:3000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

### Test from Android:
1. Start the unified backend
2. Update Android app's API base URL to point to your backend
3. Run the Android app
4. All existing features will work seamlessly!

## Migration Notes

### From `habitflow-backend` to `unified-backend`
No changes needed in Android app! The API contract is maintained.

### New Features Available
The unified backend now supports:
1. **User Authentication** - Android can now implement login/register flows
2. **Protected AI Classification** - Enhanced persona determination with Gemini AI
3. **Database Persistence** - User data and habits can be saved to MongoDB

### Optional Android Enhancements
Consider adding to your Android app:
1. Authentication flow (login/register screens)
2. Token storage (SharedPreferences or DataStore)
3. Authenticated API calls using the JWT token
4. Advanced persona classification using `/api/classify`

## Troubleshooting

### Android can't connect to backend
- Use `10.0.2.2:3000` for Android emulator (not `localhost`)
- Use your computer's IP address for physical devices
- Ensure backend is running on the correct port
- Check firewall settings

### Database connection issues
- Ensure MongoDB is installed and running
- Check MONGO_URI in .env file
- Database features will be disabled if MongoDB is not available

### AI features not working
- OPENAI_API_KEY is optional (will use mock responses)
- GEMINI_API_KEY is optional (classify endpoint requires it)
- Check API key validity in console logs

## Summary

✅ **Unified backend created successfully**
✅ **Android app is fully compatible**
✅ **All features from both backends merged**
✅ **Authentication system added**
✅ **Database persistence enabled**
✅ **Dual AI provider support (OpenAI + Gemini)**

The Android app can now communicate with the unified backend without any code changes!
