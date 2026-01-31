import { Schema, model, Document } from 'mongoose';

export interface IUser extends Document {
  email: string;
  password: string;
  persona: string | null;
  createdAt: Date;
}

const UserSchema = new Schema<IUser>({
  email: { type: String, required: true, unique: true },
  password: { type: String, required: true }, // Hashed
  persona: { type: String, default: null },
  createdAt: { type: Date, default: Date.now }
});

export default model<IUser>('User', UserSchema);
