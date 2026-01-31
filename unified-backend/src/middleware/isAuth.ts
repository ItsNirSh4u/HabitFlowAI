import { Request, Response, NextFunction } from 'express';
import jwt, { JwtPayload } from 'jsonwebtoken';

export interface AuthRequest extends Request {
  user?: JwtPayload & { _id: string };
}

const isAuth = (req: AuthRequest, res: Response, next: NextFunction): void => {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1]; // Format: Bearer <token>

  if (!token) {
    res.status(401).json({ message: 'Access Denied: No Token Provided' });
    return;
  }

  try {
    const verified = jwt.verify(token, process.env.JWT_SECRET as string) as JwtPayload;
    req.user = verified as JwtPayload & { _id: string }; // Adds user payload (e.g., _id) to request
    next();
  } catch (err) {
    res.status(400).json({ message: 'Invalid Token' });
  }
};

export default isAuth;
