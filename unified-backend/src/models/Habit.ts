import { Schema, model, Document, Types } from 'mongoose';

export interface IHabit extends Document {
  userId: Types.ObjectId;
  title: string;
  isCompleted: boolean;
  streak: number;
  history: Date[];
  createdAt: Date;
}

const HabitSchema = new Schema<IHabit>({
  userId: { type: Schema.Types.ObjectId, ref: 'User', required: true },
  title: { type: String, required: true },
  isCompleted: { type: Boolean, default: false },
  streak: { type: Number, default: 0 },
  history: [Date], // Array of completion dates
  createdAt: { type: Date, default: Date.now }
});

export default model<IHabit>('Habit', HabitSchema);
