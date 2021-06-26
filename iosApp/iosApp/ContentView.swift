import SwiftUI
import chess

struct ContentView: View {
	var body: some View {
        VStack(alignment: .leading) {
            
            HStack {
                Image("logo")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 100)
            }
            .frame(
                  minWidth: 0,
                  maxWidth: .infinity,
                  alignment: .center)
                
            Text("Play")
                .font(.headline)
                .fontWeight(.semibold)
                .foregroundColor(.orange)
            
            Text("History")
                .font(.headline)
                .fontWeight(.semibold)
                .foregroundColor(.orange)
            
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
        ContentView()
	}
}
