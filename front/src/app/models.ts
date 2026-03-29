export enum Role {
  ADMIN = 'ADMIN',
  MEMBER = 'MEMBER'
}

export enum TaskStatus {
  TODO = 'TODO',
  IN_PROGRESS = 'IN_PROGRESS',
  DONE = 'DONE'
}

export enum TaskPriority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  CRITICAL = 'CRITICAL'
}

export interface UserResponse {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: Role;
}

export interface ProjectResponse {
  id: number;
  name: string;
  description: string;
  owner: UserResponse;
  members: UserResponse[];
}

export interface TaskResponse {
  id: number;
  title: string;
  description: string;
  status: TaskStatus;
  priority: TaskPriority;
  dueDate: string;
  createdAt: string;
  updatedAt: string;
  assignee: UserResponse;
  project: ProjectResponse;
}

export interface AuthRequest {
  email: string;
  password:  string;
}

export interface AuthResponse {
  token: string;
}

export interface ProjectRequest {
  name: string;
  description: string;
  memberIds: number[];
}

export interface NewUserRequest {
  firstName: string;
  lastName: string;
  email: string;
  password:  string;
  role: Role;
}

export interface TaskRequest {
  title: string;
  description: string;
  priority: TaskPriority;
  dueDate: string;
  assigneeId: number;
  projectId: number;
}
