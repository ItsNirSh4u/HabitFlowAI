/**
 * HabitFlow Unified Backend - Main Server
 * Combines features from both backend implementations:
 * - Authentication with MongoDB (from backend/)
 * - AI integration with OpenAI and Gemini (from backend/)
 * - Persona system and motivation (from habitflow-backend/)
 * - Habit recommendations (from habitflow-backend/)
 */

import express, { Request, Response, NextFunction } from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import mongoose from 'mongoose';

// Import routes
import authRoutes from './routes/auth';
import apiRoutes from './routes/api';

// Load environment variables
dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

// ===== Middleware =====
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Request logging
app.use((req: Request, _res: Response, next: NextFunction) => {
  console.log(`${new Date().toISOString()} - ${req.method} ${req.path}`);
  next();
});

// ===== Database Connection =====
const connectDB = async () => {
  try {
    if (!process.env.MONGO_URI) {
      console.warn('⚠️  MONGO_URI not configured. Database features disabled.');
      return;
    }

    await mongoose.connect(process.env.MONGO_URI);
    console.log('✅ MongoDB Connected');
  } catch (err) {
    console.error('❌ MongoDB Connection Error:', err);
    console.log('   Continuing without database...');
  }
};

// Connect to database
connectDB();

// ===== Routes =====

// Authentication routes (from backend/)
app.use('/api/auth', authRoutes);

// Main API routes (merged from both backends)
app.use('/api', apiRoutes);

// Root endpoint - API documentation
app.get('/', (_req: Request, res: Response) => {
  res.json({
    message: 'HabitFlow AI Unified Backend API',
    version: '1.0.0',
    description: 'Complete backend with authentication, AI integration, and persona system',
    endpoints: {
      // Authentication
      register: 'POST /api/auth/register',
      login: 'POST /api/auth/login',
      
      // Personas
      personas: 'GET /api/persona/all',
      determine: 'POST /api/persona/determine',
      
      // AI Features
      motivation: 'POST /api/motivation/generate',
      recommendations: 'POST /api/habits/recommendations',
      classify: 'POST /api/classify (protected)',
      
      // Utility
      health: 'GET /api/health'
    },
    features: {
      authentication: true,
      database: !!process.env.MONGO_URI,
      openai: !!process.env.OPENAI_API_KEY,
      gemini: !!process.env.GEMINI_API_KEY
    }
  });
});

// ===== Error Handlers =====

// 404 handler
app.use((req: Request, res: Response) => {
  res.status(404).json({ 
    error: 'Endpoint not found',
    path: req.path,
    method: req.method
  });
});

// Global error handler
app.use((err: any, _req: Request, res: Response, _next: NextFunction) => {
  console.error('Error:', err);
  res.status(err.status || 500).json({ 
    error: 'Internal server error',
    message: err.message || 'An unexpected error occurred'
  });
});

// ===== Start Server =====
app.listen(PORT, () => {
  console.log('\n🚀 HabitFlow Unified Backend');
  console.log(`📡 Server running on http://localhost:${PORT}`);
  console.log(`📝 API Documentation: http://localhost:${PORT}/`);
  console.log(`✅ Environment: ${process.env.NODE_ENV || 'development'}\n`);

  // Feature status
  console.log('Features Status:');
  console.log(`  🔐 Authentication: ${process.env.MONGO_URI ? 'ENABLED' : 'DISABLED (set MONGO_URI)'}`);
  console.log(`  🗄️  Database: ${process.env.MONGO_URI ? 'ENABLED' : 'DISABLED (set MONGO_URI)'}`);
  console.log(`  🤖 OpenAI Integration: ${process.env.OPENAI_API_KEY ? 'ENABLED' : 'DISABLED (using mock)'}`);
  console.log(`  🧠 Gemini Integration: ${process.env.GEMINI_API_KEY ? 'ENABLED' : 'DISABLED'}`);
  console.log('');
});

// Graceful shutdown
process.on('SIGINT', async () => {
  console.log('\n\n👋 Shutting down gracefully...');
  await mongoose.connection.close();
  process.exit(0);
});

export default app;
