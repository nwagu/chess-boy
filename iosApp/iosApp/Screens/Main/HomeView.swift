import SwiftUI
import sharedmodels

struct HomeView: View {
    @EnvironmentObject var viewRouter: ViewRouter
    @EnvironmentObject var environment: ChessBoyEnvironment
    
    var playActions: [ViewAction] {
        [
            ViewAction(
                displayName: "Continue current game",
                action: { withAnimation { viewRouter.showPlayScreen() } }
            ),
            ViewAction(
                displayName: "New game",
                action: { withAnimation { viewRouter.navigate(screen: .newGame) } }
            ),
            ViewAction(
                displayName: "New bluetooth game",
                action: { withAnimation { viewRouter.navigate(screen: .newBluetoothGame) } }
            )
        ]
    }
    
    var historyActions: [ViewAction] {
        [
            ViewAction(
                displayName: "Recent games",
                action: { withAnimation { viewRouter.navigate(screen: .history) } }
            )
        ]
    }
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading) {
                HStack(alignment: .center, spacing: 0) {
                    Image("logo")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 100)
                }
                .frame(maxWidth: .infinity)
                Text("Play")
                    .font(.headline)
                    .fontWeight(.semibold)
                    .foregroundColor(.orange)
                    .padding(.top)
                WrappingHStack(models: playActions) { playAction in
                    QuickActionView(text: playAction.displayName) {
                        playAction.action()
                    }
                }
                Text("History")
                    .font(.headline)
                    .fontWeight(.semibold)
                    .foregroundColor(.orange)
                    .padding(.top)
                WrappingHStack(models: historyActions) { historyAction in
                    QuickActionView(text: historyAction.displayName) {
                        historyAction.action()
                    }
                }
                
                Spacer()
            }
        }
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            minHeight: 0,
            maxHeight: .infinity,
            alignment: .topLeading
        )
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        HomeView().environmentObject(ViewRouter())
    }
}
