export type MatchStatus = 'IN_PROGRESS' | 'VICTORY' | 'DEFEAT';

export interface MatchAttempt {
  sequence: string[];
  correctPositions: number;
}

export interface Match {
  id: number;
  matchStatus: MatchStatus;
  initialDate: string;
  finalDate: string | null;
  attempts: MatchAttempt[];
  correctAnswer: string[] | null;
}

export interface AttemptResponse {
  correctPositions: number;
  matchStatus: MatchStatus;
  attemptsLeft: number;
  correctAnswer?: string[] | null;
}