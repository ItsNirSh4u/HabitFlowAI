# HabitFlowAI
Academic Final Project - An AI-driven application designed to build and sustain new habits
## Project Status

### ✅ Completed Work

#### Backend Setup (TypeScript Migration)
- [x] Converted all JavaScript files to TypeScript for type safety
  - `server.ts` - Express.js server initialization
  - `src/controllers/auth.ts` - Authentication routes (register/login)
  - `src/controllers/isAuth.ts` - JWT middleware for protected routes
  - `src/models/User.ts` - MongoDB User schema with TypeScript interfaces
  - `src/models/Habit.ts` - MongoDB Habit schema with TypeScript interfaces
  - `src/services/ai_service.ts` - OpenAI GPT-4o integration for persona classification
- [x] Created `tsconfig.json` with strict type checking enabled
- [x] Updated `package.json` with:
  - TypeScript dev dependencies (@types/*)
  - Build scripts: `npm run build`, `npm run dev`, `npm start`
  - ts-node for development, nodemon for watch mode
- [x] Removed old JavaScript files to avoid conflicts
- [x] Fixed TypeScript compilation errors
- [x] Created root `.gitignore` with patterns for backend, Android, logs, and IDE files
- [x] Fixed npm audit vulnerabilities (4 vulnerabilities found in transitive dependencies)

#### Project Structure
```
HabitFlowAI/
├── backend/
│   ├── package.json (TypeScript config)
│   ├── tsconfig.json (NEW)
│   ├── server.ts (CONVERTED)
│   ├── .env (required - see setup)
│   ├── src/
│   │   ├── controllers/
│   │   │   ├── auth.ts (CONVERTED)
│   │   │   └── isAuth.ts (CONVERTED)
│   │   ├── models/
│   │   │   ├── User.ts (CONVERTED)
│   │   │   └── Habit.ts (CONVERTED)
│   │   └── services/
│   │       └── ai_service.ts (CONVERTED)
│   └── dist/ (compiled output)
├── android/ (to be created)
├── docs/
├── Instructions/
├── .gitignore (UPDATED)
└── README.md (this file)
```

---

## 📋 TODO List

### Backend Implementation
- [ ] Set up `.env` file with credentials:
  - [ ] MongoDB Atlas connection string (MONGO_URI)
  - [ ] OpenAI API key (OPENAI_API_KEY)
  - [ ] JWT secret key (JWT_SECRET)
  - [ ] PORT configuration
- [ ] Test MongoDB connection at startup
- [ ] Implement `/api/habits` routes (GET, POST, PUT, DELETE)
- [ ] Add comprehensive input validation (email, password strength, etc.)
- [ ] Implement error handling middleware
- [ ] Add request/response logging
- [ ] Write unit tests for auth and AI service
- [ ] Add API documentation (Swagger/OpenAPI)

### Testing & Validation
- [ ] Test `/api/auth/register` endpoint
- [ ] Test `/api/auth/login` endpoint
- [ ] Test `/api/classify` endpoint (persona classification)
- [ ] Test protected routes with JWT middleware
- [ ] Test error scenarios and edge cases
- [ ] Load testing for production readiness

### DevOps & Deployment
- [ ] Set up production environment variables
- [ ] Create Docker configuration for backend
- [ ] Set up CI/CD pipeline (GitHub Actions)
- [ ] Configure database backups
- [ ] Set up monitoring and logging

### Android Integration (Future)
- [ ] Create Android project structure
- [ ] Set up Kotlin + Jetpack Compose environment
- [ ] Implement authentication flow with JWT token storage
- [ ] Implement persona-based adaptive UI
- [ ] Connect to backend API endpoints
- [ ] Set up offline-first database (Room)

### Documentation
- [ ] Complete API endpoint documentation
- [ ] Write setup guide for developers
- [ ] Add architecture diagrams
- [ ] Document persona classification system
- [ ] Create Android development guide

---

## Tech Stack

### Backend
- **Runtime**: Node.js
- **Framework**: Express.js
- **Language**: TypeScript
- **Database**: MongoDB (Atlas)
- **Authentication**: JWT (jsonwebtoken)
- **Hashing**: bcrypt
- **AI**: OpenAI SDK (GPT-4o)
- **CORS**: Enabled for cross-origin requests

### Android (Planned)
- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Local DB**: Room
- **Networking**: Retrofit
- **State Management**: Kotlin Coroutines & Flow

---

## Setup Instructions

### Prerequisites
- Node.js 18+ installed
- MongoDB Atlas account
- OpenAI API key

### Backend Setup
1. Navigate to backend directory:
   ```bash
   cd backend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Create `.env` file in `backend/` directory with:
   ```
   MONGO_URI=mongodb+srv://username:password@cluster.mongodb.net/habitflowai
   JWT_SECRET=your_secret_key_here
   OPENAI_API_KEY=sk-...
   PORT=5000
   ```

4. Build TypeScript:
   ```bash
   npm run build
   ```

5. Development mode (with auto-reload):
   ```bash
   npm run dev
   ```

6. Production mode:
   ```bash
   npm start
   ```

---

## API Endpoints (Current)

### Authentication
- `POST /api/auth/register` - Register new user
  - Body: `{ email, password }`
  - Returns: `{ token, user }`
  
- `POST /api/auth/login` - Login user
  - Body: `{ email, password }`
  - Returns: `{ token, user }`

### AI & Classification
- `POST /api/classify` - Classify user persona (Protected)
  - Headers: `Authorization: Bearer <token>`
  - Body: `{ quizAnswers, goalText }`
  - Returns: `{ persona, reasoning, theme_config }`

---

## Notes
- Old JavaScript files have been removed
- All `.env` files are in `.gitignore` for security
- TypeScript strict mode is enabled for type safety
- Build output goes to `dist/` directory