# Sliding Window Frequency Counter

## Overview

This Scala application implements a Sliding Window Frequency Counter, which processes streaming numeric inputs and maintains the frequency of numbers within a time-based sliding window. It allows querying the Nth most frequent number within the specified window.

## Features
- Accepts numeric inputs in real-time.
- Maintains the frequency of numbers within a user-defined time window.
- Supports querying for the Nth highest frequency number in the last N seconds.
- Handles dynamic window sizes specified by the user at runtime.
- Efficient `O(1)` state management and `O(n)` window cleanup using a combination of Queue and HashMap.

## How It Works
1. User specifies the time window (in seconds) at the start of the program.
2. Numbers are continuously accepted from standard input.
3. Each input is stored in:
   - A Queue for tracking expiration of old data. 
   - A HashMap for maintaining frequency counts.
4. Old numbers beyond the window size are removed periodically.
5. The top-Nth frequent numbers can be queried at any time using:
    ```bash
    query <N>
    ```
6. The program continues running until the user types:
    ```bash
    exit
    ```
## Example Usage
### Input
```bash
Enter <windowSize> in seconds: 300
Enter numbers (type 'query <top-Nth>' to get Nth highest frequency, 'exit' to stop):
1
2
2
3
3
3
query 1
query 2
exit
```
### Output
```bash
Inserted: 1
Inserted: 2
Inserted: 2
Inserted: 3
Inserted: 3
Inserted: 3
Top Nth(1) item(s) by frequencies in last 300 seconds: 3
Top Nth(2) item(s) by frequencies in last 300 seconds: 2
Exiting program.
```

## Assumptions
1. Time is based on system time (Instant.now.getEpochSecond).
2. Input format: Numbers are expected as integers.
3. Queries follow the format query <N>.
4. Ties in frequency: If multiple numbers have the same frequency, all are returned.
5. Continuous Processing: The program runs in a loop until explicitly exited.

## Performance Considerations
1. State management is O(1) (using HashMap).
2. Expiring old elements is O(n) (as old elements are dequeued and state updated).
3. Querying is O(1) (since frequencies are sorted for ranking and can be queried by index).