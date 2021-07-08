import SwiftUI
import sharedmodels

struct HomeView: View {
    @StateObject var viewRouter: ViewRouter    
    
    var body: some View {
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
            
            let playActions = [
                QuickAction(displayName: "Continue current game", action: { viewRouter.navigate(screen: .play) }),
                QuickAction(displayName: "New game", action: { viewRouter.navigate(screen: .newGame) }),
                QuickAction(displayName: "New bluetooth game", action: { viewRouter.navigate(screen: .newBluetoothGame) })
            ]
            
            FlowRow(views: playActions)
            
            Text("History")
                .font(.headline)
                .fontWeight(.semibold)
                .foregroundColor(.orange)
            
            let historyActions = [
                QuickAction(displayName:"Recent games", action: { viewRouter.navigate(screen: .history) })
            ]
            
            FlowRow(views: historyActions)
            
            Spacer()
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
        HomeView(viewRouter: ViewRouter())
    }
}
