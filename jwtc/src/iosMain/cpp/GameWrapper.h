#import <Foundation/Foundation.h>

@class GameWrapper;

@interface GameWrapper

+ (void) newGame;
+ (void) fen:(NSString*)fen;
+ (void) searchDepth:(int)depth;
+ (int) bestMove;

@end
