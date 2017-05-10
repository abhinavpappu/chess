import java.util.ArrayList;
/**
 * Network.java  
 *
 * @author:
 * Assignment #:
 * 
 * Brief Program Description:
 * 
 *
 */
public class Network
{
    private ArrayList<ArrayList<Neuron>> neurons;
    private int inputs;
    /**
     * constructs the network of neurons as a nested arraylist
     */
    public Network(int[] size, int inputs)
    {
        neurons = new ArrayList<ArrayList<Neuron>>();
        this.inputs = inputs;
        for (int r = 0; r < size.length; r++)
        {
            ArrayList<Neuron> temp = new ArrayList<Neuron>();
            for (int c = 0; c < size[r]; c++)
            {
                if (r==0) //if outerlayer
                {
                    temp.add(new Neuron(inputs, r, c, this));
                }
                else //if inner layer
                {
                    temp.add(new Neuron(size[r-1], r, c, this));
                }
            }
            neurons.add(temp);
        }
    }
    /**
     * feeds forward every neuron, and determines the logistic based on weight
     */
    public double[] feed(double[] inputs)
    {   
        double[] output = new double[1];
        double[] temp;
        for (int r = 0; r < neurons.size(); r++)
        {
            if (r==0) //if outerlayer
            {
                output = new double[neurons.get(r).size()];
                for (int c = 0; c < neurons.get(r).size(); c++)
                {
                    output[c] = (neurons.get(r).get(c)).feed(inputs);
                }
            }
            else //if hiddenlayer
            {
                temp = new double[neurons.get(r).size()];
                for (int c = 0; c < neurons.get(r).size(); c++)
                {
                    temp[c] = (neurons.get(r).get(c)).feed(output);
                }
                output=temp;
            }
        }
        return output;
    }
    public void train(double[] inputs, double[] desired)
    {
        feed(inputs);
        for (int r = neurons.size()-1; r >= 0; r--)
        {
            for (int c = neurons.get(r).size()-1; c >= 0; c--)
            {
                neurons.get(r).get(c).train(inputs, desired);
            }
        }
        
        for (ArrayList<Neuron> r: neurons)
        {
            for (Neuron n: r)
            {
                n.updateWeights();
            }
        }
    }

    public ArrayList<ArrayList<Neuron>> getNeurons()
    {
        return neurons;
    }
}
