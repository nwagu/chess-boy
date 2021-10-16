import SwiftUI
import sharedmodels

struct HomeView: View {
    @EnvironmentObject var environment: ChessBoyEnvironment
    
    var playActionViews: [IdentifiableView] {
        [
            IdentifiableView(view: AnyView(QuickActionView(
                text: "Continue current game",
                onClickAction: { environment.showPlayScreen() }
            ))),
            IdentifiableView(view: AnyView(QuickActionNavigationView(
                text: "New game",
                destination: AnyView(NewGameView())
            ))),
            IdentifiableView(view: AnyView(QuickActionNavigationView(
                text: "New bluetooth game",
                destination: AnyView(NewBluetoothGameView())
            )))
        ]
    }
    
    var historyActionViews: [IdentifiableView] {
        [
            IdentifiableView(view: AnyView(QuickActionNavigationView(
                text: "Recent games",
                destination: AnyView(HistoryView())
            )))
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
                WrappingHStack(models: playActionViews) { playActionView in
                    playActionView.view
                }
                Text("History")
                    .font(.headline)
                    .fontWeight(.semibold)
                    .foregroundColor(.orange)
                    .padding(.top)
                WrappingHStack(models: historyActionViews) { historyActionView in
                    historyActionView.view
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
        HomeView()
    }
}
