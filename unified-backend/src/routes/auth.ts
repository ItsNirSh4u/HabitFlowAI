import { Router, Request, Response } from 'express';
import bcrypt from 'bcrypt';
import jwt from 'jsonwebtoken';
import User from '../models/User';
import { RegisterRequest, LoginRequest, AuthResponse } from '../types';
import { console } from 'inspector/promises';

const router = Router();

/**
 * POST /api/auth/register
 * Register a new user
 */
router.post('/register', async (req: Request<{}, {}, RegisterRequest>, res: Response) => {
  try {
    console.log('Received registration request');
    const { email, password } = req.body;
    console.log('Registration request received for email:', email);

    // Validate input
    if (!email || !password) {
      return res.status(400).json({ message: 'Email and password are required' });
    }
    console.log('Input validation passed');
    if (password.length < 6) {
      return res.status(400).json({ message: 'Password must be at least 6 characters' });
    }
    console.log('Password length validation passed');
    // Check if user exists
    const existingUser = await User.findOne({ email });
    if (existingUser) {
      return res.status(400).json({ message: 'User already exists' });
    }
    console.log('No existing user found, proceeding to create new user');
    // Hash password
    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(password, salt);
    console.log('Password hashed successfully');
    // Create user
    const newUser = new User({
      email,
      password: hashedPassword
    });
    console.log('New user instance created');
    const savedUser = await newUser.save();
    console.log('New user saved to database with ID:', savedUser._id);
    // Create Token
    const token = jwt.sign(
      { _id: savedUser._id }, 
      process.env.JWT_SECRET as string, 
      { expiresIn: '24h' }
    );
    console.log('JWT token generated');
    const response: AuthResponse = {
      token,
      user: {
        id: savedUser._id.toString(),
        email: savedUser.email,
        persona: savedUser.persona
      }
    };
    console.log('Registration successful, sending response');
    return res.status(201).json(response);
  } catch (err) {
    const error = err instanceof Error ? err.message : 'Unknown error';
    console.error('Registration error:', error);
    return res.status(500).json({ error: 'Registration failed' });
  }
});

/**
 * POST /api/auth/login
 * Login a user
 */
router.post('/login', async (req: Request<{}, {}, LoginRequest>, res: Response) => {
  try {
    const { email, password } = req.body;

    // Validate input
    if (!email || !password) {
      return res.status(400).json({ message: 'Email and password are required' });
    }

    // Find user
    const user = await User.findOne({ email });
    if (!user) {
      return res.status(400).json({ message: 'Invalid credentials' });
    }

    // Verify password
    const validPass = await bcrypt.compare(password, user.password);
    if (!validPass) {
      return res.status(400).json({ message: 'Invalid credentials' });
    }

    // Create token
    const token = jwt.sign(
      { _id: user._id }, 
      process.env.JWT_SECRET as string, 
      { expiresIn: '24h' }
    );

    const response: AuthResponse = {
      token,
      user: {
        id: user._id.toString(),
        email: user.email,
        persona: user.persona
      }
    };

    return res.json(response);
  } catch (err) {
    const error = err instanceof Error ? err.message : 'Unknown error';
    console.error('Login error:', error);
    return res.status(500).json({ error: 'Login failed' });
  }
});

export default router;
