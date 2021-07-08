import SwiftUI
import sharedmodels

struct HomeView: View {
    @EnvironmentObject var viewRouter: ViewRouter
    
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
                QuickAction(
                    displayName: "Continue current game",
                    action: { withAnimation { viewRouter.navigate(screen: .play) } }
                ),
                QuickAction(
                    displayName: "New game",
                    action: { withAnimation { viewRouter.navigate(screen: .newGame) } }
                ),
                QuickAction(
                    displayName: "New bluetooth game",
                    action: { withAnimation { viewRouter.navigate(screen: .newBluetoothGame) } }
                )
            ]
            
            FlowRow(views: playActions)
            
            Text("History")
                .font(.headline)
                .fontWeight(.semibold)
                .foregroundColor(.orange)
            
            let historyActions = [
                QuickAction(
                    displayName:"Recent games",
                    action: { withAnimation { viewRouter.navigate(screen: .history) } }
                )
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
        HomeView().environmentObject(ViewRouter())
    }
}
