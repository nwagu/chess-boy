#include "GameWrapper.h"
#include "Game.h"

@implementation GameWrapper

static Game *stGame = NULL;

+ (void) newGame {
    stGame = new Game();
    stGame->reset();
}
+ (void) fen:(NSString*)fen {

}
+ (void) searchDepth:(int)depth {
    stGame->searchLimited(depth);
}
+ (int) bestMove {
    return 0;
}
@end