/**
 * Logger Utility
 * Centralized logging with different log levels
 */

export enum LogLevel {
  DEBUG = 0,
  INFO = 1,
  WARN = 2,
  ERROR = 3,
  NONE = 4,
}

interface LogEntry {
  level: LogLevel
  message: string
  data?: any
  timestamp: string
  context?: string
}

class Logger {
  private logLevel: LogLevel
  private logs: LogEntry[] = []
  private maxLogs = 100

  constructor() {
    // Set log level from environment or default to INFO in production, DEBUG in development
    const envLevel = import.meta.env.VITE_LOG_LEVEL || (import.meta.env.DEV ? 'DEBUG' : 'INFO')
    this.logLevel = LogLevel[envLevel as keyof typeof LogLevel] ?? LogLevel.INFO
  }

  private shouldLog(level: LogLevel): boolean {
    return level >= this.logLevel
  }

  private formatMessage(level: LogLevel, message: string, data?: any, context?: string): string {
    const levelName = LogLevel[level]
    const contextStr = context ? `[${context}]` : ''
    const dataStr = data ? ` ${JSON.stringify(data, null, 2)}` : ''
    return `${levelName} ${contextStr} ${message}${dataStr}`
  }

  private log(level: LogLevel, message: string, data?: any, context?: string): void {
    if (!this.shouldLog(level)) return

    const entry: LogEntry = {
      level,
      message,
      data,
      timestamp: new Date().toISOString(),
      context,
    }

    // Store log entry
    this.logs.push(entry)
    if (this.logs.length > this.maxLogs) {
      this.logs.shift()
    }

    // Console output with appropriate method
    const formattedMessage = this.formatMessage(level, message, data, context)
    
    switch (level) {
      case LogLevel.DEBUG:
        console.debug(formattedMessage, data || '')
        break
      case LogLevel.INFO:
        console.info(formattedMessage, data || '')
        break
      case LogLevel.WARN:
        console.warn(formattedMessage, data || '')
        break
      case LogLevel.ERROR:
        console.error(formattedMessage, data || '')
        break
    }
  }

  debug(message: string, data?: any, context?: string): void {
    this.log(LogLevel.DEBUG, message, data, context)
  }

  info(message: string, data?: any, context?: string): void {
    this.log(LogLevel.INFO, message, data, context)
  }

  warn(message: string, data?: any, context?: string): void {
    this.log(LogLevel.WARN, message, data, context)
  }

  error(message: string, data?: any, context?: string): void {
    this.log(LogLevel.ERROR, message, data, context)
  }

  // Get recent logs
  getLogs(level?: LogLevel, limit?: number): LogEntry[] {
    let filtered = this.logs

    if (level !== undefined) {
      filtered = filtered.filter((log) => log.level === level)
    }

    if (limit) {
      filtered = filtered.slice(-limit)
    }

    return filtered
  }

  // Clear logs
  clearLogs(): void {
    this.logs = []
  }

  // Set log level
  setLogLevel(level: LogLevel): void {
    this.logLevel = level
  }

  // Get current log level
  getLogLevel(): LogLevel {
    return this.logLevel
  }
}

export const logger = new Logger()
export default logger

