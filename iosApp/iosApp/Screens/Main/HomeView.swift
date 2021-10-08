import SwiftUI
import sharedmodels

struct HomeView: View {
    @EnvironmentObject var viewRouter: ViewRouter
    @EnvironmentObject var environment: ChessBoyEnvironment
    
    var playActions =
        [
            DestinationViewAction(
                displayName: "Continue current game",
                destination: {  AnyView(NewGameView()) }
            ),
            DestinationViewAction(
                displayName: "New game",
                destination: {  AnyView(NewGameView()) }
            ),
            DestinationViewAction(
                displayName: "New bluetooth game",
                destination: {  AnyView(NewBluetoothGameView()) }
            )
        ]
    
    
    var historyActions =
        [
            DestinationViewAction(
                displayName: "Recent games",
                destination: {  AnyView(HistoryView()) }
            )
        ]
    
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
                    QuickActionView(text: playAction.displayName, destinationGenerator: playAction.destination)
                }
                Text("History")
                    .font(.headline)
                    .fontWeight(.semibold)
                    .foregroundColor(.orange)
                    .padding(.top)
                WrappingHStack(models: historyActions) { historyAction in
                    QuickActionView(text: historyAction.displayName, destinationGenerator: historyAction.destination)
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
