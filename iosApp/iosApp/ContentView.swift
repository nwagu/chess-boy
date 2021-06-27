import SwiftUI
import chess

struct ContentView: View {
    
    @State private var isShowingNewGameView = false
    @State private var isShowingNewBluetoothGameView = false
    @State private var isShowingHistoryView = false
    
	var body: some View {
        NavigationView {
            VStack(alignment: .leading) {
                
                NavigationLink(destination: NewGameView(), isActive: $isShowingNewGameView) { EmptyView() }
                NavigationLink(destination: NewBluetoothGameView(), isActive: $isShowingNewBluetoothGameView) { EmptyView() }
                NavigationLink(destination: HistoryView(), isActive: $isShowingHistoryView) { EmptyView() }
                    
                Text("Play")
                    .font(.headline)
                    .fontWeight(.semibold)
                    .foregroundColor(.orange)
                
                let playActions = [
                    QuickAction(displayName: "Continue current game", action: { isShowingHistoryView = true }),
                    QuickAction(displayName: "New game", action: { isShowingNewGameView = true }),
                    QuickAction(displayName: "New bluetooth game", action: { isShowingNewBluetoothGameView = true })
                ]
                
                FlowRow(views: playActions)
                
                Text("History")
                    .font(.headline)
                    .fontWeight(.semibold)
                    .foregroundColor(.orange)
                
                let historyActions = [
                    QuickAction(displayName:"Recent games", action: {isShowingHistoryView = true})
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
            .navigationBarTitle(Text("Chess Boy"), displayMode: .inline)
        }

    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
        ContentView()
	}
}
