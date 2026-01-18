/**
 * HabitFlow Backend - Main Server
 * Express TypeScript server with AI integration
 */

import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import apiRoutes from './routes/api';

// Load environment variables
dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Request logging
app.use((req, res, next) => {
  console.log(`${new Date().toISOString()} - ${req.method} ${req.path}`);
  next();
});

// API Routes
app.use('/api', apiRoutes);

// Root endpoint
app.get('/', (req, res) => {
  res.json({
    message: 'HabitFlow AI Backend API',
    version: '1.0.0',
    endpoints: {
      health: '/api/health',
      personas: '/api/persona/all',
      determine: 'POST /api/persona/determine',
      motivation: 'POST /api/motivation/generate',
      recommendations: 'POST /api/habits/recommendations'
    }
  });
});

// 404 handler
app.use((req, res) => {
  res.status(404).json({ error: 'Endpoint not found' });
});

// Error handler
app.use((err: any, req: express.Request, res: express.Response, next: express.NextFunction) => {
  console.error('Error:', err);
  res.status(500).json({ error: 'Internal server error' });
});

// Start server
app.listen(PORT, () => {
  console.log(`🚀 HabitFlow Backend running on http://localhost:${PORT}`);
  console.log(`📝 API Documentation available at http://localhost:${PORT}/`);
  console.log(`✅ Environment: ${process.env.NODE_ENV || 'development'}`);

  if (process.env.OPENAI_API_KEY) {
    console.log('🤖 OpenAI API integration: ENABLED');
  } else {
    console.log('🤖 OpenAI API integration: DISABLED (using mock responses)');
  }
});

export default app;

