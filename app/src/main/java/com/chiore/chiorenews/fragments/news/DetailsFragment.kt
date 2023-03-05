package com.chiore.chiorenews.fragments.news

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.chiore.chiorenews.R
import com.chiore.chiorenews.data.model.Article
import com.chiore.chiorenews.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var detailsData: Article
    private var isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.detailsData?.let {
            detailsData = it

            binding.apply {

                Glide.with(requireView()).load(detailsData.urlToImage)
                    .error(R.drawable.no_details_image).into(binding.ivDetails)

                tvDetailsTitle.text = detailsData.title
                tvDetailsAuthor.text = detailsData.author
                tvDetailsPublished.text = detailsData.publishedAt
                tvDetailsDescription.text = detailsData.description

                icBack.setOnClickListener {
                    findNavController().navigateUp()
                }

                btnReadAll.setOnClickListener { view ->
                    val action = DetailsFragmentDirections
                        .actionDetailsFragmentToWebFragment(detailsData.url)
                    Navigation.findNavController(view).navigate(action)
                }

                ivWebPage.setOnClickListener { view ->
                    val action = DetailsFragmentDirections
                        .actionDetailsFragmentToWebFragment(detailsData.url)
                    Navigation.findNavController(view).navigate(action)
                }

                icFav.setOnClickListener {
                    isFavorite = !isFavorite
                    if (!isFavorite) {
                        icFav.setBackgroundResource(R.drawable.ic_fav_empty)
                    } else {
                        icFav.setBackgroundResource(R.drawable.ic_fav)
                    }
                }

                icShare.setOnClickListener {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, detailsData.url)

                    val chooser = Intent.createChooser(intent, "Share using...")
                    startActivity(chooser)
                }

            }

        }
    }

}